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
        project.ext.SwaggerDocgen = SwaggerDocgen

        project.task('generateSwaggerCode',
            type: SwaggerCodegen,
            group: 'build',
            description: 'Generates a source code from the swagger specification.')

        project.afterEvaluate {
            project.tasks.withType(SwaggerCodegen) { task ->
                SwaggerCodegenHelp.injectHelpTaskFor(task)
            }
            project.tasks.withType(SwaggerDocgen) { task ->
                SwaggerDocgen.injectGenerationTasksFor(task)
            }
        }
    }

}
