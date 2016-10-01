package org.hidetake.gradle.swagger.codegen

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * A task to show help of swagger-codegen.
 *
 * @author Hidetake Iwata
 */
class SwaggerCodegenHelp extends DefaultTask {

    @TaskAction
    void show() {
        project.javaexec {
            classpath = project.configurations.swaggerCodegen
            main = 'io.swagger.codegen.SwaggerCodegen'
        }

        project.tasks.withType(SwaggerCodegen).collect { task ->
            println("Available JSON configuration for $task:")
            project.javaexec {
                classpath = project.configurations.swaggerCodegen
                main = 'io.swagger.codegen.SwaggerCodegen'
                args('config-help', '-l', task.language)
            }
        }
    }

}
