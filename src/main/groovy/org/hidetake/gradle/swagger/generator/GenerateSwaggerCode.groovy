package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * A task to generate a source code from the Swagger specification.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerCode extends DefaultTask {

    @Input
    String language

    @SkipWhenEmpty @InputFiles
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    String library

    @Optional @InputFile
    File configFile

    @Optional @InputDirectory
    File templateDir

    @Optional @Input
    Map<String, String> additionalProperties

    @Optional @Input
    List<String> components

    def GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
    }

    @TaskAction
    void exec() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"
        if (components) {
            assert components.every { component ->
                component in ['models', 'apis', 'supportingFiles']
            }
        }

        assert outputDir != project.projectDir, 'Prevent wiping the project directory'

        project.delete(outputDir)
        outputDir.mkdirs()

        def args = buildOptions()
        def systemProperties = components?.collectEntries { component ->
            [(component): '']
        }
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
        options
    }

}
