package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerCodeSpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateSwaggerCode')
    }

    def "plugin should throw exception if projectDir is given"() {
        given:
        def path = GenerateSwaggerCodeSpec.getResource('/petstore-invalid.yaml').path
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

    def "plugin should throw exception if no dependency of Swagger Codegen is given"() {
        given:
        def path = GenerateSwaggerCodeSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerCode.language = 'java'
            tasks.generateSwaggerCode.inputFile = file(path)
            tasks.generateSwaggerCode.exec()
        }

        then:
        IllegalStateException e = thrown()
        e.message.contains('swaggerCodegen')
    }

    def "plugin should build options for Swagger Codegen"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def options = project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerCode.language = 'java'
            tasks.generateSwaggerCode.inputFile = new File('input')
            tasks.generateSwaggerCode.outputDir = new File('output')
            tasks.generateSwaggerCode.additionalProperties = [foo: 'bar', baz: 'zzz']
            tasks.generateSwaggerCode.buildOptions()
        }

        then:
        options == [
            'generate',
            '-l', 'java',
            '-i', 'input',
            '-o', 'output',
            '--additional-properties', 'foo=bar,baz=zzz'
        ]
    }

    @Unroll
    def "plugin should system properties on components=#givenComponents"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def systemProperties = project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerCode.components = givenComponents
            tasks.generateSwaggerCode.buildSystemProperties()
        }

        then:
        systemProperties == expectedSystemProperties

        where:
        givenComponents                                         | expectedSystemProperties
        null                                                    | null
        []                                                      | [:]
        ['models']                                              | [models: '']
        ['models', 'apis']                                      | [models: '', apis: '']
        [models: true]                                          | [models: '']
        [models: false]                                         | [models: 'false']
        [models: null]                                          | [models: 'false']
        [models: 'User']                                        | [models: 'User']
        [models: ['User']]                                      | [models: 'User']
        [models: 'User,Pet']                                    | [models: 'User,Pet']
        [models: ['User', 'Pet']]                               | [models: 'User,Pet']
        [models: 'User', supportingFiles: 'StringUtil.java']    | [models: 'User', supportingFiles: 'StringUtil.java']
    }

}
