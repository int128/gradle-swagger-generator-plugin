package org.hidetake.gradle.swagger.generator

import com.fasterxml.jackson.databind.JsonNode
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

    @SkipWhenEmpty @InputFiles
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    Map<String, Object> options

    @Optional @Input
    String header

    def GenerateSwaggerUI() {
        outputDir = new File(project.buildDir, 'swagger-ui')
        options = [:]
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

        assert outputDir != project.projectDir, 'Prevent wiping the project directory'

        // Validate before extract
        def inputJson = Mappers.YAML.readTree(inputFile)

        project.delete(outputDir)
        outputDir.mkdirs()

        extractWebJar()
        replaceSwaggerUiLoader(inputJson)
        if (header) {
            insertCustomHeader()
        }
    }

    private void extractWebJar() {
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
        ] << options
        def swaggerUIoptionsString = Mappers.JSON.writeValueAsString(swaggerUIoptions)
        def inputJsonString = Mappers.JSON.writeValueAsString(inputJson)
        def customLoaderScript = """\
            // Overwrite options
            \$.each(
                $swaggerUIoptionsString,
                function (key, value) { window.swaggerUi.setOption(key, value) }
            );
            // Set Swagger JSON
            window.swaggerUi.setOption('spec', $inputJsonString);
            // Load UI
            window.swaggerUi.load();
            """
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
