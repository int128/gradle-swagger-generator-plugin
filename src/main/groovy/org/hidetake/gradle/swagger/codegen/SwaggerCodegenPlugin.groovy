package org.hidetake.gradle.swagger.codegen

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin to generate code with Swagger.
 *
 * @author Hidetake Iwata
 */
class SwaggerCodegenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.configurations.create('swaggerCodegen')
        project.ext.SwaggerCodegen = SwaggerCodegen
        project.task('swaggerCodegenHelp', type: SwaggerCodegenHelp)
    }

}
