package org.hidetake.gradle.swagger.generator

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.util.Json
import io.swagger.util.Yaml
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.*

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

    @Optional @Input
    Map<String, Object> options

    @Optional @InputFiles
    String header

    def GenerateSwaggerUI() {
        outputDir = new File(project.buildDir, 'swagger-ui')
        options = [:]
        onlyIf {
            inputFile
        }
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

        // Validate before extract
        def inputJson = Yaml.mapper().readTree(inputFile)

        extractWebJar()
        replaceSwaggerUiLoader(inputJson)
        if (header) {
            insertCustomHeader()
        }
    }

    private void extractWebJar() {
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
    }

    private void replaceSwaggerUiLoader(JsonNode inputJson) {
        def swaggerUIoptions = [
            url         : '',
            validatorUrl: null,
            spec        : inputJson,
        ] << options
        def customLoaderScript = """\
            // Overwrite options
            \$.each(
                ${Json.mapper().valueToTree(swaggerUIoptions)},
                function (key, value) { window.swaggerUi.setOption(key, value) }
            );
            // Load
            window.swaggerUi.load();
            """.stripIndent()
        def index = new File(outputDir, 'index.html')
        index.text = index.text.replace('window.swaggerUi.load();', customLoaderScript)
    }

    private void insertCustomHeader() {
        assert header
        def startOfScript = '<script type="text/javascript">'
        def index = new File(outputDir, 'index.html')
        index.text = index.text.replace(startOfScript, "$header\n$startOfScript")
    }

}
