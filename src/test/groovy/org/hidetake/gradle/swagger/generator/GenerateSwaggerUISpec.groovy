package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class GenerateSwaggerUISpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateSwaggerUI == GenerateSwaggerUI
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateSwaggerUI')
    }

    def "exception should be thrown if no dependency given"() {
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

    def "exception should be thrown if projectDir is given"() {
        given:
        def path = GenerateSwaggerUISpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:2.2.6'
            }
            tasks.generateSwaggerUI.inputFile = file(path)
            tasks.generateSwaggerUI.outputDir = projectDir
            tasks.generateSwaggerUI.exec()
        }

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
