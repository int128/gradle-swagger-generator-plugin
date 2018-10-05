package org.hidetake.gradle.swagger.generator.codegen


import spock.lang.Specification

class V2ExecutorSpec extends Specification {

    def "buildGenerateArgs should build arguments"() {
        given:
        def options = new GenerateOptions(
            language: 'java',
            inputFile: 'input',
            outputDir: 'output',
            additionalProperties: [foo: 'bar', baz: 'zzz'],
        )

        when:
        def args = V2Executor.buildGenerateArgs(options)

        then:
        args == [
            'generate',
            '-l', 'java',
            '-i', 'input',
            '-o', 'output',
            '--additional-properties', 'foo=bar,baz=zzz'
        ]
    }

    def "buildGenerateArgs should build raw options"() {
        given:
        def options = new GenerateOptions(
            language: 'java',
            inputFile: 'input',
            outputDir: 'output',
            rawOptions: ['--verbose'],
        )

        when:
        def args = V2Executor.buildGenerateArgs(options)

        then:
        args == [
            'generate',
            '-l', 'java',
            '-i', 'input',
            '-o', 'output',
            '--verbose'
        ]
    }

}
