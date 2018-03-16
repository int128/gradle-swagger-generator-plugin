package org.hidetake.gradle.swagger.generator

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class Fixture {
    static Project projectWithPlugin(@DelegatesTo(Project) Closure closure = {}) {
        def project = ProjectBuilder.builder().build()
        project.apply(plugin: 'org.hidetake.swagger.generator')
        project.with(closure)
        project
    }

    static File file(YAML yaml) {
        new File(Fixture.getResource("/${yaml}.yaml").path)
    }

    static enum YAML {
        petstore,
        petstore_invalid,
    }
}
