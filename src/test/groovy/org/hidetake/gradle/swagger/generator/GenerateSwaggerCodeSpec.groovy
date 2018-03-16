package org.hidetake.gradle.swagger.generator

import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerCodeSpec extends Specification {

    def "GenerateSwaggerCode class should be available in a build script"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
    }

    def "plugin should add default task"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.tasks.findByName('generateSwaggerCode')
    }

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def project = Fixture.projectWithPlugin {
            repositories {
                jcenter()
            }
            dependencies {
                swaggerCodegen 'io.swagger:swagger-codegen-cli:2.3.1'
            }
            generateSwaggerCode {
                language = 'java'
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = buildDir
                wipeOutputDir = wipe
            }
        }

        and: 'create a file in the outputDir'
        project.buildDir.mkdirs()
        def keep = new File(project.buildDir, 'keep') << 'something'

        when:
        project.tasks.generateSwaggerCode.exec()

        then:
        keep.exists() == existence

        where:
        wipe    | verb      | existence
        true    | 'wipe'    | false
        false   | 'keep'    | true
    }

    def "task should fail if outputDir == projectDir"() {
        given:
        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = projectDir
            }
        }

        when:
        project.tasks.generateSwaggerCode.exec()

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

    def "task should fail if swaggerCodegen dependency is not set"() {
        given:
        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = Fixture.file(Fixture.YAML.petstore)
            }
        }

        when:
        project.tasks.generateSwaggerCode.exec()

        then:
        IllegalStateException e = thrown()
        e.message.contains('swaggerCodegen')
    }

    def "task should build options"() {
        given:
        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = new File('input')
                outputDir = new File('output')
                additionalProperties = [foo: 'bar', baz: 'zzz']
            }
        }

        when:
        def options = project.tasks.generateSwaggerCode.buildOptions()

        then:
        options == [
            'generate',
            '-l', 'java',
            '-i', 'input',
            '-o', 'output',
            '--additional-properties', 'foo=bar,baz=zzz'
        ]
    }

    def "task should build raw options"() {
        given:
        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                rawOptions = ['--verbose']
                inputFile = new File('input')
                outputDir = new File('output')
            }
        }

        when:
        def options = project.tasks.generateSwaggerCode.buildOptions()

        then:
        options == [
            'generate',
            '-l', 'java',
            '-i', 'input',
            '-o', 'output',
            '--verbose'
        ]
    }

    @Unroll
    def "task should set system properties on components=#givenComponents"() {
        given:
        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                components = givenComponents
            }
        }

        when:
        def systemProperties = project.tasks.generateSwaggerCode.buildSystemProperties()

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
