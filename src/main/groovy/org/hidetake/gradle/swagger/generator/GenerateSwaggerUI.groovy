package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.*
import org.hidetake.gradle.swagger.generator.util.Mappers
import org.hidetake.gradle.swagger.generator.util.Resources

/**
 * A task to generate Swagger UI.
 *
 * @author Hidetake Iwata
 */
@Slf4j
@CacheableTask
class GenerateSwaggerUI extends DefaultTask {

    @SkipWhenEmpty @InputFile @PathSensitive(PathSensitivity.NAME_ONLY)
    File inputFile

    @OutputDirectory
    File outputDir

    @Input
    boolean wipeOutputDir = true

    @Optional @Input @Deprecated
    Map<String, Object> options = [:]

    @Optional @Input @Deprecated
    String header

    GenerateSwaggerUI() {
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

        // TODO: remove in the future release
        if (options) {
            log.warn('WARNING: GenerateSwaggerUI.options is no longer supported. See https://github.com/int128/gradle-swagger-generator-plugin/issues/81')
        }
        if (header) {
            log.warn('WARNING: GenerateSwaggerUI.header is no longer supported. See https://github.com/int128/gradle-swagger-generator-plugin/issues/81')
        }

        if (wipeOutputDir) {
            assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            project.delete(outputDir)
        }
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
        new File(outputDir, 'swagger-spec.js').withWriter('UTF-8') { writer ->
            writer.append('window.swaggerSpec=')
            Mappers.JSON.writeValue(writer, inputJson)
        }
    }

    private void buildIndexHtml() {
        Resources.withInputStream('/swagger-ui.html') { inputStream ->
            new File(outputDir, 'index.html').withOutputStream { outputStream ->
                outputStream << inputStream
            }
        }
    }

}
