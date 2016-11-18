package org.hidetake.gradle.swagger.generator

import org.gradle.api.tasks.*

/**
 * A task to generate a source code from the Swagger specification.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerCode extends JavaExec {

    @Input
    String language

    @InputFile
    File inputFile

    @Optional @OutputDirectory
    File outputDir

    @Optional @Input
    String library

    @Optional @InputFile
    File configFile

    @Optional @InputDirectory
    File templateDir

    @Optional @Input
    List<String> components

    def GenerateSwaggerCode() {
        outputDir = new File(project.buildDir, 'swagger-code')
    }

    @TaskAction
    @Override
    void exec() {
        main = 'io.swagger.codegen.SwaggerCodegen'
        classpath(project.configurations.swaggerCodegen)
        args(buildOptions())
        if (components) {
            assert components.every { component ->
                component in ['models', 'apis', 'supportingFiles']
            }
            systemProperties(components.collectEntries { component ->
                [component, '']
            })
        }

        if (classpath.empty) {
            throw new IllegalStateException('''\
                Dependency for swagger-codegen-cli should be given as follows:
                  dependencies {
                    swaggerCodegen 'io.swagger:swagger-codegen-cli:x.x.x'
                  }'''.stripIndent())
        }

        project.delete(outputDir)
        outputDir.mkdirs()
        super.exec()
    }

    private List<String> buildOptions() {
        assert language, "language should be set in the task $name"
        assert inputFile, "inputFile should be set in the task $name"
        assert outputDir, "outputDir should be set in the task $name"

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
        options
    }

}
