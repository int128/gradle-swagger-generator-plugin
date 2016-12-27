package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ValidateSwaggerSpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.ValidateSwagger == ValidateSwagger
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('validateSwagger')
    }

    def "ValidationException should be thrown if invalid YAML is given"() {
        given:
        def path = ValidateSwaggerSpec.getResource('/petstore-invalid.yaml').path
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
