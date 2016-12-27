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
        project.GenerateSwaggerUI == GenerateSwaggerUI
        project.ValidateSwagger == ValidateSwagger
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
        project.tasks.findByName('generateSwaggerUI')
        project.tasks.findByName('validateSwagger')
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

    def "exception should be thrown if projectDir is given in SwaggerUI"() {
        given:
        def path = SwaggerGeneratorPluginSpec.getResource('/petstore-invalid.yaml').path
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

    def "exception should be thrown if projectDir is given in generateSwaggerCode"() {
        given:
        def path = SwaggerGeneratorPluginSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerCode.language = 'java'
            tasks.generateSwaggerCode.inputFile = file(path)
            tasks.generateSwaggerCode.outputDir = projectDir
            tasks.generateSwaggerCode.exec()
        }

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
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
