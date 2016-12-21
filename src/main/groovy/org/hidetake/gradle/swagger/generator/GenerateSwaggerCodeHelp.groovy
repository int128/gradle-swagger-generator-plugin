package org.hidetake.gradle.swagger.generator

import io.swagger.codegen.SwaggerCodegen
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
        onlyIf {
            language
        }
    }

    @TaskAction
    void exec() {
        println("Available JSON configuration for language $language:")
        SwaggerCodegen.main('config-help', '-l', language)
    }

    static Task injectHelpTaskFor(GenerateSwaggerCode task) {
        task.project.task("${task.name}Help",
            description: "Displays available JSON configuration for $task",
            dependsOn: task.dependsOn,
            group: 'help',
            type: GenerateSwaggerCodeHelp) {
            language = task.language
        }
    }

}
