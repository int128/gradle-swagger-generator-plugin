package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.*

/**
 * A task to generate Swagger UI.
 *
 * @author Hidetake Iwata
 */
@Slf4j
class GenerateSwaggerUI extends DefaultTask {

    @SkipWhenEmpty @InputFiles
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input @Deprecated
    Map<String, Object> options = [:]

    @Optional @Input @Deprecated
    String header

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

        assert outputDir != project.projectDir, 'Prevent wiping the project directory'

        // TODO: remove in the future release
        if (options) {
            log.warn('WARNING: GenerateSwaggerUI.options is no longer supported. See https://github.com/int128/gradle-swagger-generator-plugin/issues/81')
        }
        if (header) {
            log.warn('WARNING: GenerateSwaggerUI.header is no longer supported. See https://github.com/int128/gradle-swagger-generator-plugin/issues/81')
        }

        project.delete(outputDir)
        outputDir.mkdirs()

        extractWebJar()
        buildSwaggerSpec()
        buildIndexHtml()
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

    private void buildSwaggerSpec() {
        def inputJson = Mappers.YAML.readTree(inputFile)
        new File(outputDir, 'swagger-spec.js').withWriter { writer ->
            writer.append('window.swaggerSpec=')
            Mappers.JSON.writeValue(writer, inputJson)
        }
    }

    private void buildIndexHtml() {
        def htmlResource = GenerateSwaggerUI.getResourceAsStream('/swagger-ui.html')
        assert htmlResource, 'swagger-ui.html should be exist in resource'
        htmlResource.withStream { inputStream ->
            new File(outputDir, 'index.html').withOutputStream { outputStream ->
                outputStream << inputStream
            }
        }
    }

}
