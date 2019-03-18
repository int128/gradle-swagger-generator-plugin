package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical
import spock.lang.Specification
import spock.lang.Unroll

class Swagger2AdaptorSpec extends Specification {
    @Canonical
    static class TestCase {
        GenerateOptions generateOptions
        JavaExecOptions javaExecOptions
    }

    private static final SLF4J_PROPERTIES = Helper.slf4jSimpleSystemProperties()

    @Unroll
    def "generate should build options with #c.generateOptions"() {
        given:
        def expected = c.javaExecOptions

        when:
        def actual = new Swagger2Adaptor().generate(c.generateOptions)

        then:
        actual == expected

        where:
        c << [
            new TestCase(
                generateOptions: new GenerateOptions(
                    generatorFiles: [new File('foo.jar'), new File('foo.pom')],
                    language: 'java',
                    inputFile: 'input',
                    outputDir: 'output',
                ),
                javaExecOptions: new JavaExecOptions(
                    classpath: [new File('foo.jar')],
                    main: Swagger2Adaptor.CLASS_NAME,
                    args: [
                        'generate',
                        '-l', 'java',
                        '-i', 'input',
                        '-o', 'output',
                    ],
                    systemProperties: SLF4J_PROPERTIES,
                ),
            ),
            new TestCase(
                generateOptions: new GenerateOptions(
                    generatorFiles: [],
                    language: 'java',
                    inputFile: 'input',
                    outputDir: 'output',
                    library: 'spring',
                    configFile: 'config',
                    templateDir: 'template',
                    additionalProperties: [foo: 'bar', baz: 'zzz'],
                    rawOptions: ['--verbose'],
                    systemProperties: [bar: 'foo'],
                ),
                javaExecOptions: new JavaExecOptions(
                    classpath: [],
                    main: Swagger2Adaptor.CLASS_NAME,
                    args: [
                        'generate',
                        '-l', 'java',
                        '-i', 'input',
                        '-o', 'output',
                        '--library', 'spring',
                        '-c', 'config',
                        '-t', 'template',
                        '--additional-properties', 'foo=bar,baz=zzz',
                        '--verbose',
                    ],
                    systemProperties: SLF4J_PROPERTIES + [bar: 'foo'],
                ),
            ),
        ]
    }

    def 'help should build options'() {
        given:
        def options = new HelpOptions(
            generatorFiles: [new File('foo.jar'), new File('foo.pom')],
        )
        def expected = new JavaExecOptions(
            classpath: [new File('foo.jar')],
            main: Swagger2Adaptor.CLASS_NAME,
            args: ['help', 'generate'],
            systemProperties: SLF4J_PROPERTIES,
        )

        when:
        def actual = new Swagger2Adaptor().help(options)

        then:
        actual == expected
    }

    def 'configHelp should build options'() {
        given:
        def options = new ConfigHelpOptions(
            generatorFiles: [new File('foo.jar'), new File('foo.pom')],
            language: 'java',
        )
        def expected = new JavaExecOptions(
            classpath: [new File('foo.jar')],
            main: Swagger2Adaptor.CLASS_NAME,
            args: ['config-help', '-l', 'java'],
            systemProperties: SLF4J_PROPERTIES,
        )

        when:
        def actual = new Swagger2Adaptor().configHelp(options)

        then:
        actual == expected
    }
}
