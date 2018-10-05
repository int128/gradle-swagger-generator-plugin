package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.hidetake.gradle.swagger.generator.codegen.ExecutorFactory
import org.hidetake.gradle.swagger.generator.codegen.GenerateOptions

/**
 * A task to generate a source code from the Swagger specification.
 *
 * @author Hidetake Iwata
 */
@CacheableTask
class GenerateSwaggerCode extends DefaultTask {

    @SkipWhenEmpty @InputFiles
    File inputFile

    @Input
    String language

    @OutputDirectory
    File outputDir

    @Optional @Input
    boolean wipeOutputDir = true

    @Optional @Input
    String library

    @Optional @InputFile
    File configFile

    @Optional @InputDirectory
    File templateDir

    @Optional @Input
    Map<String, String> additionalProperties

    @Optional @Input
    def components

    @Optional @Input
    List<String> rawOptions

    def GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
    }

    @TaskAction
    void exec() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"

        if (wipeOutputDir) {
            assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            project.delete(outputDir)
        }
        outputDir.mkdirs()

        final urls = project.configurations.swaggerCodegen.resolve()*.toURI()*.toURL() as URL[]
        final executor = ExecutorFactory.instance.getExecutor(project.buildscript.classLoader, urls)
        executor.generate(buildOptions())
    }

    GenerateOptions buildOptions() {
        new GenerateOptions(
            inputFile: inputFile.path,
            language: language,
            outputDir: outputDir.path,
            library: library,
            configFile: configFile?.path,
            templateDir: templateDir?.path,
            additionalProperties: additionalProperties,
            rawOptions: rawOptions,
            systemProperties: buildSystemProperties(),
        )
    }

    Map<String, String> buildSystemProperties() {
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
