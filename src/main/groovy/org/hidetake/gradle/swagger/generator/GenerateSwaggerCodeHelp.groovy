package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaExecSpec
import org.hidetake.gradle.swagger.generator.codegen.AdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.ConfigHelpOptions
import org.hidetake.gradle.swagger.generator.codegen.DefaultAdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.HelpOptions

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
@Slf4j
@CacheableTask
class GenerateSwaggerCodeHelp extends DefaultTask {

    @Input
    String language

    @Optional
    @Input
    def configuration

    @Internal
    AdaptorFactory adaptorFactory = DefaultAdaptorFactory.instance

    GenerateSwaggerCodeHelp() {
        onlyIf { language }
    }

    @TaskAction
    void exec() {
        assert language, "language should be set in the task $name"

        def generatorFiles = GenerateSwaggerCode.Helper.configuration(project, configuration).resolve()
        def adaptor = adaptorFactory.findAdaptor(generatorFiles)
        if (adaptor == null) {
            throw new IllegalStateException('''\
                Add a generator dependency to the project. For example:
                  dependencies {
                      swaggerCodegen 'io.swagger:swagger-codegen-cli:2.x.x'             // Swagger Codegen V2
                      swaggerCodegen 'io.swagger.codegen.v3:swagger-codegen-cli:3.x.x'  // or Swagger Codegen V3
                      swaggerCodegen 'org.openapitools:openapi-generator-cli:3.x.x'     // or OpenAPI Generator.
                  }'''.stripIndent())
        }

        System.err.println("=== Available rawOptions ===")
        def helpOptions = new HelpOptions(
            generatorFiles: generatorFiles,
        )
        def helpJavaExecOptions = adaptor.help(helpOptions)
        log.info("JavaExecOptions: $helpJavaExecOptions")
        project.javaexec { JavaExecSpec c ->
            c.classpath(helpJavaExecOptions.classpath)
            c.main = helpJavaExecOptions.main
            c.args = helpJavaExecOptions.args
            c.systemProperties(helpJavaExecOptions.systemProperties)
        }

        System.err.println("=== Available JSON configuration for language $language ===")
        def configHelpOptions = new ConfigHelpOptions(
            generatorFiles: generatorFiles,
            language: language,
        )
        def configHelpJavaExecOptions = adaptor.configHelp(configHelpOptions)
        log.info("JavaExecOptions: $configHelpJavaExecOptions")
        project.javaexec { JavaExecSpec c ->
            c.classpath(configHelpJavaExecOptions.classpath)
            c.main = configHelpJavaExecOptions.main
            c.args = configHelpJavaExecOptions.args
            c.systemProperties(configHelpJavaExecOptions.systemProperties)
        }
    }

    static Task injectHelpTaskFor(GenerateSwaggerCode task) {
        task.project.task("${task.name}Help",
            description: "Displays available JSON configuration for $task",
            group: 'help',
            type: GenerateSwaggerCodeHelp) {
            language = task.language
            configuration = task.configuration
        }
    }

}
