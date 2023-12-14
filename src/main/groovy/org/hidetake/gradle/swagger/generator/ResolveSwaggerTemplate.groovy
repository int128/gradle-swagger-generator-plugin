package org.hidetake.gradle.swagger.generator

import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Sync

import javax.inject.Inject

/**
 * A task to extract template files from a dependency.
 */
abstract class ResolveSwaggerTemplate extends Sync {

    @Inject
    abstract ArchiveOperations getArchives();

    ResolveSwaggerTemplate() {
        from(project.configurations.swaggerTemplate.elements.map {
            it.collect { zipFile ->
                archives.zipTree(zipFile)
            }
        })
        into("${project.buildDir}/swagger-template")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

}
