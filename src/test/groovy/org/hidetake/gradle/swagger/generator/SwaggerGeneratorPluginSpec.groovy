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

    def "exception should be thrown if no dependency given in SwaggerCodegen"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            generateSwaggerCode {
                inputFile = file('nothing.yaml')
                language = 'java'
            }
            tasks.generateSwaggerCode.exec()
        }

        then:
        thrown(IllegalStateException)
    }

    def "exception should be thrown if no dependency given in SwaggerUI"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerUI.exec()
        }

        then:
        thrown(IllegalStateException)
    }

    def "ValidationException should be thrown if invalid YAML is given"() {
        given:
        def path = SwaggerGeneratorPluginSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.validateSwagger.inputFile = file(path)
            tasks.validateSwagger.exec()
        }

        then:
        thrown(ValidationException)
    }

}
