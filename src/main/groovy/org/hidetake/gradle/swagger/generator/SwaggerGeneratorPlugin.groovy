package org.hidetake.gradle.swagger.generator

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin to generate code with Swagger.
 *
 * @author Hidetake Iwata
 */
class SwaggerGeneratorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.configurations.create('swaggerCodegen')

        project.ext.SwaggerCodegen = SwaggerCodegen

        project.afterEvaluate {
            project.tasks.withType(SwaggerCodegen) { task ->
                SwaggerCodegenHelp.injectHelpTaskFor(task)
            }
        }
    }

}
