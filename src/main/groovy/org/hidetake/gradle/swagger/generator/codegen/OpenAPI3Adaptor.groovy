package org.hidetake.gradle.swagger.generator.codegen

/**
 * An adaptor for OpenAPI Generator V3.
 *
 * See: https://github.com/OpenAPITools/openapi-generator
 */
class OpenAPI3Adaptor implements Adaptor {
    static final String CLASS_NAME = 'org.openapitools.codegen.OpenAPIGenerator'

    @Override
    JavaExecOptions generate(GenerateOptions options) {
        def args = []
        args << 'generate'
        args << '-g' << options.language
        args << '-i' << options.inputFile
        args << '-o' << options.outputDir
        if (options.library) {
            args << '--library' << options.library
        }
        if (options.configFile) {
            args << '-c' << options.configFile
        }
        if (options.templateDir) {
            args << '-t' << options.templateDir
        }
        if (options.additionalProperties) {
            args << '--additional-properties' << options.additionalProperties.collect { key, value ->
                "$key=$value"
            }.join(',')
        }
        if (options.rawOptions) {
            args.addAll(options.rawOptions)
        }

        def systemProperties = [:]
        systemProperties.putAll(Helper.slf4jSimpleSystemProperties())
        if (options.systemProperties) {
            systemProperties.putAll(options.systemProperties)
        }

        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: args,
            main: CLASS_NAME,
            systemProperties: systemProperties,
        )
    }

    @Override
    JavaExecOptions help(HelpOptions options) {
        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: ['help', 'generate'],
            main: CLASS_NAME,
            systemProperties: Helper.slf4jSimpleSystemProperties(),
        )
    }

    @Override
    JavaExecOptions configHelp(ConfigHelpOptions options) {
        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: ['config-help', '-g', options.language],
            main: CLASS_NAME,
            systemProperties: Helper.slf4jSimpleSystemProperties(),
        )
    }
}
