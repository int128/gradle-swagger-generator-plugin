package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * A task to generate Swagger UI.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerUI extends DefaultTask {

    @InputFile
    File inputFile

    @OutputDirectory
    File outputDir

    def GenerateSwaggerUI() {
        outputDir = new File(project.buildDir, 'swagger-ui')
    }

    @TaskAction
    void exec() {
        if (project.configurations.swaggerUI.dependencies.empty) {
            throw new IllegalStateException('''\
                Dependency for swagger-ui should be given as follows:
                  dependencies {
                    swaggerUI 'org.webjars:swagger-ui:x.x.x'
                  }'''.stripIndent())
        }

        project.delete(outputDir)
        outputDir.mkdirs()

        project.copy {
            into(outputDir)
            from(inputFile)
            project.configurations.swaggerUI.each { jar ->
                from(project.zipTree(jar)) {
                    include('META-INF/resources/webjars/swagger-ui/*/')
                    eachFile { FileCopyDetails details ->
                        details.relativePath = new RelativePath(true,
                            details.relativePath.segments.drop(5))
                    }
                }
            }
        }

        def index = new File(outputDir, 'index.html')
        index.text = index.text.replace(
            'http://petstore.swagger.io/v2/swagger.json',
            inputFile.name
        )
    }

}
