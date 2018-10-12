package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.hidetake.gradle.swagger.generator.codegen.ExecutorFactory

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
@CacheableTask
class GenerateSwaggerCodeHelp extends DefaultTask {

    @Input
    String language

    def GenerateSwaggerCodeHelp() {
        onlyIf { language }
    }

    @TaskAction
    void exec() {
        final urls = project.configurations.swaggerCodegen.resolve()*.toURI()*.toURL() as URL[]
        final executor = ExecutorFactory.instance.getExecutor(project.buildscript.classLoader, urls)

        System.err.println("=== Available rawOptions ===")
        executor.generateHelp()

        System.err.println("=== Available JSON configuration for language $language ===")
        executor.configHelp(language)
    }

    static Task injectHelpTaskFor(GenerateSwaggerCode task) {
        task.project.task("${task.name}Help",
            description: "Displays available JSON configuration for $task",
            group: 'help',
            type: GenerateSwaggerCodeHelp) {
            language = task.language
        }
    }

}
