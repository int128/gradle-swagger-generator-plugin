package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerCodeHelp extends DefaultTask {

    @Input
    String language

    def GenerateSwaggerCodeHelp() {
        onlyIf { language }
    }

    @TaskAction
    void exec() {
        println("=== Available rawOptions ===")
        SwaggerCodegenExecutor.getInstance(project).execute(['help', 'generate'])

        println("=== Available JSON configuration for language $language ===")
        SwaggerCodegenExecutor.getInstance(project).execute(['config-help', '-l', language])
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
