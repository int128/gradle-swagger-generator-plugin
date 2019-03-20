package org.hidetake.gradle.swagger.generator

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Fixture {
    static Project projectWithPlugin(@DelegatesTo(Project) Closure closure = {}) {
        def project = ProjectBuilder.builder().build()
        project.apply(plugin: 'org.hidetake.swagger.generator')
        project.with(closure)
        project
    }

    /**
     * Create a JAR file with given contents.
     *
     * @param destination JAR file
     * @param contents map of filename and content
     */
    static void createJAR(File destination, Map<String, String> contents) {
        destination.withOutputStream {
            new ZipOutputStream(it).withStream { zip ->
                contents.each { filename, content ->
                    zip.putNextEntry(new ZipEntry(filename))
                    zip << content
                    zip.closeEntry()
                }
            }
        }
    }

    static File file(YAML yaml) {
        new File(Fixture.getResource("/${yaml}.yaml").path)
    }

    static enum YAML {
        petstore,
        petstore_invalid,
    }
}
