package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.process.ExecOperations
import org.gradle.process.JavaExecSpec
import org.hidetake.gradle.swagger.generator.codegen.AdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.DefaultAdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.GenerateOptions
import org.hidetake.gradle.swagger.generator.codegen.JavaExecOptions

import javax.inject.Inject

/**
 * A task to generate a source code from the Swagger specification.
 */
@Slf4j
@CacheableTask
abstract class GenerateSwaggerCode extends DefaultTask {

    @SkipWhenEmpty @InputFile @PathSensitive(PathSensitivity.NAME_ONLY)
    File inputFile

    @Input
    String language

    @OutputDirectory
    File outputDir

    @Input
    boolean wipeOutputDir = true

    @Optional
    @Input
    String library

    @Optional @InputFile @PathSensitive(PathSensitivity.NAME_ONLY)
    File configFile

    @Optional @InputDirectory @PathSensitive(PathSensitivity.NAME_ONLY)
    File templateDir

    @Optional
    @Input
    Map<String, String> additionalProperties

    @Optional
    @Input
    def components

    @Optional
    @Input
    List<String> rawOptions

    @Optional
    @Input
    List<String> jvmArgs

    @Optional
    @Nested
    abstract Property<JavaLauncher> getLauncher()

    @Classpath
    abstract ConfigurableFileCollection getConfiguration()

    @Internal
    AdaptorFactory adaptorFactory = DefaultAdaptorFactory.instance

    @Inject
    abstract ExecOperations getExec()

    @Inject
    abstract FileOperations getFiles()

    @Inject
    abstract JavaToolchainService getJavaToolchainService()

    GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
        configuration.from(project.configurations.swaggerCodegen)
        project.plugins.withId("java") {
            def toolchain = project.extensions.getByType(JavaPluginExtension).toolchain
            def defaultLauncher = getJavaToolchainService().launcherFor(toolchain)
            launcher.convention(defaultLauncher)
        }
    }

    @TaskAction
    void exec() {
        def javaExecOptions = execInternal()
        log.info("JavaExecOptions: $javaExecOptions")
        exec.javaexec { JavaExecSpec c ->
            c.classpath(javaExecOptions.classpath)
            c.mainClass = javaExecOptions.main
            c.args = javaExecOptions.args
            c.systemProperties(javaExecOptions.systemProperties)
            c.jvmArgs(javaExecOptions.jvmArgs ?: [])
            if (launcher.isPresent()) {
                c.executable = launcher.get().executablePath.asFile
            }
        }
    }

    JavaExecOptions execInternal() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"

        if (wipeOutputDir) {
            // assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            files.delete(outputDir)
        }
        outputDir.mkdirs()

        def generateOptions = new GenerateOptions(
            generatorFiles: getConfiguration().files,
            inputFile: inputFile.path,
            language: language,
            outputDir: outputDir.path,
            library: library,
            configFile: configFile?.path,
            templateDir: templateDir?.path,
            additionalProperties: additionalProperties,
            rawOptions: rawOptions,
            jvmArgs: this.jvmArgs,
            systemProperties: Helper.systemProperties(components),
        )
        log.info("GenerateOptions: $generateOptions")

        def adaptor = adaptorFactory.findAdaptor(generateOptions.generatorFiles)
        if (adaptor == null) {
            throw new IllegalStateException('''\
                Add a generator dependency to the project. For example:
                  dependencies {
                      swaggerCodegen 'io.swagger:swagger-codegen-cli:2.x.x'             // Swagger Codegen V2
                      swaggerCodegen 'io.swagger.codegen.v3:swagger-codegen-cli:3.x.x'  // or Swagger Codegen V3
                      swaggerCodegen 'org.openapitools:openapi-generator-cli:3.x.x'     // or OpenAPI Generator.
                  }'''.stripIndent())
        }
        adaptor.generate(generateOptions)
    }

    protected static class Helper {
        static Map<String, String> systemProperties(components) {
            if (components instanceof Collection) {
                components.collectEntries { k -> [(k as String): ''] }
            } else if (components instanceof Map) {
                components.collectEntries { k, v ->
                    if (v instanceof Collection) {
                        [(k as String): v.join(',')]
                    } else if (v == true) {
                        [(k as String): '']
                    } else if (v == false || v == null) {
                        [(k as String): 'false']
                    } else {
                        [(k as String): v as String]
                    }
                } as Map<String, String>
            } else if (components == null) {
                [:]
            } else {
                throw new IllegalArgumentException("components must be Collection or Map")
            }
        }
    }

}
