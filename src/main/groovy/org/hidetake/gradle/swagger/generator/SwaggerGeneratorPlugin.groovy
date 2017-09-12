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
        project.configurations.create('swaggerUI')
        project.configurations.create('swaggerTemplate')

        project.ext.GenerateSwaggerCode = GenerateSwaggerCode
        project.ext.GenerateSwaggerUI = GenerateSwaggerUI
        project.ext.ValidateSwagger = ValidateSwagger

        project.task('resolveSwaggerTemplate',
            type: ResolveSwaggerTemplate,
            group: 'build setup',
            description: 'Resolves template files from the swaggerTemplate configuration.')

        project.task('generateSwaggerCode',
            type: GenerateSwaggerCode,
            group: 'build',
            description: 'Generates a source code from the swagger specification.',
            dependsOn: 'resolveSwaggerTemplate')

        project.task('generateSwaggerUI',
            type: GenerateSwaggerUI,
            group: 'documentation',
            description: 'Generates Swagger UI from the swagger specification.')

        project.task('validateSwagger',
            type: ValidateSwagger,
            group: 'verification',
            description: 'Validates the swagger specification.')

        project.afterEvaluate {
            project.tasks.withType(GenerateSwaggerCode) { task ->
                GenerateSwaggerCodeHelp.injectHelpTaskFor(task)
            }
        }
    }

}
