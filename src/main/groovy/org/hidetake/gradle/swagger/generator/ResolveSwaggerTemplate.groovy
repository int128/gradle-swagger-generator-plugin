package org.hidetake.gradle.swagger.generator

import org.gradle.api.tasks.Sync

/**
 * A task to extract template files from a dependency.
 */
class ResolveSwaggerTemplate extends Sync {

    ResolveSwaggerTemplate() {
        from {
            project.configurations.swaggerTemplate.resolve().collect {
                project.zipTree(it)
            }
        }
        into("${project.buildDir}/swagger-template")
    }

}
