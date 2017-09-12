package org.hidetake.gradle.swagger.generator

import org.gradle.api.tasks.Sync

/**
 * A task to extract template files from a dependency.
 *
 * @author Hidetake Iwata
 */
class ResolveSwaggerTemplate extends Sync {

    def ResolveSwaggerTemplate() {
        from {
            if (!project.configurations.swaggerTemplate.empty) {
                project.zipTree(project.configurations.swaggerTemplate.singleFile)
            } else {
                []
            }
        }
        into("${project.buildDir}/swagger-template")
    }

}
