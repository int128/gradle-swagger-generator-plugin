package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SwaggerGeneratorPluginSpec extends Specification {

    def "plugin should provide SwaggerCodegen as a class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.SwaggerCodegen == SwaggerCodegen
    }

}
