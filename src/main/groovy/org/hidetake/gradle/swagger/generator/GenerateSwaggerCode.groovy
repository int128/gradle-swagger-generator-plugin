package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * A task to generate a source code from the Swagger specification.
 *
 * @author Hidetake Iwata
 */
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

        def args = buildOptions()
        def systemProperties = buildSystemProperties()
        SwaggerCodegenExecutor.getInstance(project).execute(systemProperties, args)
    }

    List<String> buildOptions() {
        def options = []
        options << 'generate'
        options << '-l' << language
        options << '-i' << inputFile.path
        options << '-o' << outputDir.path
        if (library) {
            options << '--library' << library
        }
        if (configFile) {
            options << '-c' << configFile.path
        }
        if (templateDir) {
            options << '-t' << templateDir.path
        }
        if (additionalProperties) {
            options << '--additional-properties' << additionalProperties.collect { key, value ->
                "$key=$value"
            }.join(',')
        }
        if (rawOptions) {
            options.addAll(rawOptions)
        }
        options
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
            null
        } else {
            throw new IllegalArgumentException("components must be Collection or Map")
        }
    }

}
