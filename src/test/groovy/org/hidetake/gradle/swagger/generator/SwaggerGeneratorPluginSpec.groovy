package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SwaggerGeneratorPluginSpec extends Specification {

    def "plugin should provide task classes"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
        project.GenerateSwaggerDoc == GenerateSwaggerDoc
    }

    def "plugin should add default tasks"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateSwaggerCode')
        project.tasks.findByName('generateSwaggerDoc')
    }

}
