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
        def swaggerSources = project.container(SwaggerSource)
        project.extensions.add('swaggerSources', swaggerSources)

        project.configurations.create('swaggerCodegen')
        project.configurations.create('swaggerUI')
        project.configurations.create('swaggerTemplate')

        project.ext.GenerateSwaggerCode = GenerateSwaggerCode
        project.ext.GenerateSwaggerUI = GenerateSwaggerUI
        project.ext.GenerateReDoc = GenerateReDoc
        project.ext.ValidateSwagger = ValidateSwagger

        project.task('resolveSwaggerTemplate',
            type: ResolveSwaggerTemplate,
            group: 'build setup',
            description: 'Resolves template files from the swaggerTemplate configuration.')

        createValidateSwagger(project)
        createGenerateSwaggerCode(project)
        createGenerateSwaggerUI(project)
        createGenerateReDoc(project)

        swaggerSources.all {
            def swaggerSource = delegate as SwaggerSource
            swaggerSource.validation = createValidateSwagger(project, swaggerSource.name)
            swaggerSource.code = createGenerateSwaggerCode(project, swaggerSource.name)
            swaggerSource.ui = createGenerateSwaggerUI(project, swaggerSource.name)
            swaggerSource.reDoc = createGenerateReDoc(project, swaggerSource.name)

            project.tasks.validateSwagger.dependsOn(swaggerSource.validation)
            project.tasks.generateSwaggerCode.dependsOn(swaggerSource.code)
            project.tasks.generateSwaggerUI.dependsOn(swaggerSource.ui)
            project.tasks.generateReDoc.dependsOn(swaggerSource.reDoc)

            swaggerSource.validation.reportFile = new File(project.buildDir, "swagger-validation-${swaggerSource.name}.yaml")
            swaggerSource.code.outputDir = new File(project.buildDir, "swagger-code-${swaggerSource.name}")
            swaggerSource.ui.outputDir = new File(project.buildDir, "swagger-ui-${swaggerSource.name}")
            swaggerSource.reDoc.outputDir = new File(project.buildDir, "redoc-${swaggerSource.name}")
        }

        project.afterEvaluate {
            project.tasks.withType(GenerateSwaggerCode) { task ->
                GenerateSwaggerCodeHelp.injectHelpTaskFor(task)
            }
        }
    }

    private static createValidateSwagger(Project project, String sourceName = null) {
        project.task("validateSwagger${sourceName ? sourceName.capitalize() : ''}",
            type: ValidateSwagger,
            group: 'verification',
            description: "Validates YAML of ${sourceName ?: 'the OpenAPI specification'}.") as ValidateSwagger
    }

    private static createGenerateSwaggerCode(Project project, String sourceName = null) {
        project.task("generateSwaggerCode${sourceName ? sourceName.capitalize() : ''}",
            type: GenerateSwaggerCode,
            group: 'build',
            description: "Generates a source code from ${sourceName ?: 'the OpenAPI specification'}.",
            dependsOn: 'resolveSwaggerTemplate') as GenerateSwaggerCode
    }

    private static createGenerateSwaggerUI(Project project, String sourceName = null) {
        project.task("generateSwaggerUI${sourceName ? sourceName.capitalize() : ''}",
            type: GenerateSwaggerUI,
            group: 'documentation',
            description: "Generates Swagger UI from ${sourceName ?: 'the OpenAPI specification'}.") as GenerateSwaggerUI
    }

    private static createGenerateReDoc(Project project, String sourceName = null) {
        project.task("generateReDoc${sourceName ? sourceName.capitalize() : ''}",
            type: GenerateReDoc,
            group: 'documentation',
            description: "Generates ReDoc from ${sourceName ?: 'the OpenAPI specification'}.") as GenerateReDoc
    }

}
