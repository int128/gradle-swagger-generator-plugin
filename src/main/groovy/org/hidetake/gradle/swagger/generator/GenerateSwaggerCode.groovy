package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.*
import org.hidetake.gradle.swagger.generator.codegen.AdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.DefaultAdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.GenerateOptions
import org.hidetake.gradle.swagger.generator.codegen.JavaExecOptions

/**
 * A task to generate a source code from the Swagger specification.
 */
@Slf4j
@CacheableTask
class GenerateSwaggerCode extends DefaultTask {

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
    def configuration

    @Internal
    AdaptorFactory adaptorFactory = DefaultAdaptorFactory.instance

    GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
    }

    @TaskAction
    void exec() {
        def javaExecOptions = execInternal()
        log.info("JavaExecOptions: $javaExecOptions")
        project.javaexec {
            classpath(javaExecOptions.classpath)
            main = javaExecOptions.main
            args = javaExecOptions.args
            systemProperties(javaExecOptions.systemProperties)
        }
    }

    JavaExecOptions execInternal() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"

        if (wipeOutputDir) {
            assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            project.delete(outputDir)
        }
        outputDir.mkdirs()

        def generateOptions = new GenerateOptions(
            generatorFiles: Helper.configuration(project, configuration).resolve(),
            inputFile: inputFile.path,
            language: language,
            outputDir: outputDir.path,
            library: library,
            configFile: configFile?.path,
            templateDir: templateDir?.path,
            additionalProperties: additionalProperties,
            rawOptions: rawOptions,
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
        static Configuration configuration(Project project, configuration) {
            switch (configuration) {
                case null:
                    return project.configurations.swaggerCodegen
                case String:
                    return project.configurations.getByName(configuration)
                case Configuration:
                    return configuration
            }
            throw new IllegalArgumentException("configuration must be String or org.gradle.api.artifacts.Configuration but unknown type: ${configuration}")
        }

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
