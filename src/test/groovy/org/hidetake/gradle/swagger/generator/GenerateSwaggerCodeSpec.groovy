package org.hidetake.gradle.swagger.generator

import org.hidetake.gradle.swagger.generator.codegen.Adaptor
import org.hidetake.gradle.swagger.generator.codegen.AdaptorFactory
import org.hidetake.gradle.swagger.generator.codegen.GenerateOptions
import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerCodeSpec extends Specification {

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def mockAdaptorFactory = Mock(AdaptorFactory)
        mockAdaptorFactory.findAdaptor(_) >> Mock(Adaptor)

        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = new File('something')
                outputDir = buildDir
                wipeOutputDir = wipe
                adaptorFactory = mockAdaptorFactory
            }

            buildDir.mkdirs()
            new File(buildDir, 'keep') << 'something'
        }

        when:
        project.tasks.generateSwaggerCode.execInternal()

        then:
        new File(project.buildDir, 'keep').exists() == existence

        where:
        wipe    | verb      | existence
        true    | 'wipe'    | false
        false   | 'keep'    | true
    }

    def "task should fail if outputDir == projectDir"() {
        given:
        def mockAdaptorFactory = Mock(AdaptorFactory)
        mockAdaptorFactory.findAdaptor(_) >> Mock(Adaptor)

        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = new File('something')
                outputDir = projectDir
                adaptorFactory = mockAdaptorFactory
            }
        }

        when:
        project.tasks.generateSwaggerCode.execInternal()

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

    def "task should call the adaptor"() {
        given:
        def mockAdaptor = Mock(Adaptor)
        0 * mockAdaptor.generate(_)
        1 * mockAdaptor.generate(new GenerateOptions(
            generatorFiles: [],
            inputFile: 'input',
            language: 'java',
            outputDir: 'output',
            additionalProperties: [foo: 'bar', baz: 'zzz'],
            systemProperties: [:],
        ))

        def mockAdaptorFactory = Mock(AdaptorFactory)
        mockAdaptorFactory.findAdaptor(_) >> mockAdaptor

        def project = Fixture.projectWithPlugin {
            generateSwaggerCode {
                language = 'java'
                inputFile = new File('input')
                outputDir = new File('output')
                additionalProperties = [foo: 'bar', baz: 'zzz']
                adaptorFactory = mockAdaptorFactory
            }
        }

        when:
        project.tasks.generateSwaggerCode.execInternal()

        then:
        noExceptionThrown()
    }

    @Unroll
    def "task should set system properties on components=#givenComponents"() {
        when:
        def systemProperties = GenerateSwaggerCode.Helper.systemProperties(givenComponents)

        then:
        systemProperties == expectedSystemProperties

        where:
        givenComponents                                         | expectedSystemProperties
        null                                                    | [:]
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
