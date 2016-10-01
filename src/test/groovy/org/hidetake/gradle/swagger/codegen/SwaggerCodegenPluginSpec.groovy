package org.hidetake.gradle.swagger.codegen

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SwaggerCodegenPluginSpec extends Specification {

    def "plugin should provide swaggerCodegenHelp task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.codegen'
        }

        then:
        project.tasks.getByPath('swaggerCodegenHelp')
    }

    def "plugin should provide SwaggerCodegen as a class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.codegen'
        }

        then:
        project.SwaggerCodegen == SwaggerCodegen
    }

}
