package org.hidetake.gradle.swagger.generator

import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskAction

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
class SwaggerCodegenHelp extends JavaExec {

    @Input
    String language

    @TaskAction
    @Override
    void exec() {
        classpath = project.configurations.swaggerCodegen
        main = 'io.swagger.codegen.SwaggerCodegen'
        args('config-help', '-l', language)
        println("Available JSON configuration for language $language:")
        super.exec()
    }

    static Task injectHelpTaskFor(SwaggerCodegen task) {
        task.project.task("${task.name}Help",
            description: "Displays available JSON configuration for $task",
            group: 'help',
            type: SwaggerCodegenHelp) {
            language = task.language
        }
    }

}
