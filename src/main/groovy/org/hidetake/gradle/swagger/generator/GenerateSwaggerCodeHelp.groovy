package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.JavaExecSpec
import org.hidetake.gradle.swagger.generator.codegen.AdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.ConfigHelpOptions
import org.hidetake.gradle.swagger.generator.codegen.DefaultAdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.HelpOptions

import javax.inject.Inject

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
@Slf4j
@CacheableTask
abstract class GenerateSwaggerCodeHelp extends DefaultTask {

    @Input
    @Optional
    String language

    @Classpath
    abstract ConfigurableFileCollection getConfiguration()

    @Optional
    @Input
    List<String> jvmArgs

    @Inject
    abstract ExecOperations getExec();

    @Internal
    AdaptorFactory adaptorFactory = DefaultAdaptorFactory.instance

    GenerateSwaggerCodeHelp() {
        configuration.from(project.configurations.swaggerCodegen)
    }

    @TaskAction
    void exec() {
        if (language == null) {
            return
        }

        def generatorFiles = configuration.files
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
            jvmArgs: this.jvmArgs
        )
        def helpJavaExecOptions = adaptor.help(helpOptions)
        log.info("JavaExecOptions: $helpJavaExecOptions")
        exec.javaexec { JavaExecSpec c ->
            c.classpath(helpJavaExecOptions.classpath)
            c.main = helpJavaExecOptions.main
            c.args = helpJavaExecOptions.args
            c.systemProperties(helpJavaExecOptions.systemProperties)
            c.jvmArgs(helpJavaExecOptions.jvmArgs ?: [])
        }

        System.err.println("=== Available JSON configuration for language $language ===")
        def configHelpOptions = new ConfigHelpOptions(
            generatorFiles: generatorFiles,
            language: language,
            jvmArgs: this.jvmArgs
        )
        def configHelpJavaExecOptions = adaptor.configHelp(configHelpOptions)
        log.info("JavaExecOptions: $configHelpJavaExecOptions")
        exec.javaexec { JavaExecSpec c ->
            c.classpath(configHelpJavaExecOptions.classpath)
            c.main = configHelpJavaExecOptions.main
            c.args = configHelpJavaExecOptions.args
            c.systemProperties(configHelpJavaExecOptions.systemProperties)
            c.jvmArgs(configHelpJavaExecOptions.jvmArgs ?: [])
        }
    }

    static Task injectHelpTaskFor(GenerateSwaggerCode task) {
        task.project.task("${task.name}Help",
            description: "Displays available JSON configuration for $task",
            group: 'help',
            type: GenerateSwaggerCodeHelp) {
            language = task.language
            configuration.from(task.configuration)
        }
    }

}
