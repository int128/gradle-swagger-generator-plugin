package org.hidetake.gradle.swagger.generator

import io.swagger.util.Json
import io.swagger.util.Yaml
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

        def inputJson = Yaml.mapper().readTree(inputFile)

        project.delete(outputDir)
        outputDir.mkdirs()

        project.copy {
            into(outputDir)
            project.configurations.swaggerUI.each { jar ->
                from(project.zipTree(jar)) {
                    include('META-INF/resources/webjars/swagger-ui/*/')
                    exclude('**/*.gz')
                    eachFile { FileCopyDetails details ->
                        details.relativePath = new RelativePath(true,
                            details.relativePath.segments.drop(5))
                    }
                }
            }
        }

        def options = [
            url: '',
            spec: inputJson,
        ]
        def customLoaderScript = """
// Overwrite options
\$.each(
    ${Json.mapper().valueToTree(options)},
    function (key, value) { window.swaggerUi.setOption(key, value) }
)
// Load
window.swaggerUi.load();
"""

        def index = new File(outputDir, 'index.html')
        index.text = index.text.replace('window.swaggerUi.load();', customLoaderScript)
    }

}
