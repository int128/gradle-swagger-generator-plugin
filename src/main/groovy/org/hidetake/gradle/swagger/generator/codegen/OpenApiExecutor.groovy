package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

/**
 * An executor of OpenAPI Generator
 */
@Slf4j
class OpenApiExecutor implements Executor {
	static final String CLASS_NAME = 'org.openapitools.codegen.OpenAPIGenerator'
	private static final lock = new Object()

	private final Class clazz

	OpenApiExecutor(Class clazz) {
		this.clazz = clazz
	}

	@Override
	void generate(GenerateOptions options) {
		execute(buildGenerateArgs(options), options.systemProperties)
	}

	static buildGenerateArgs(GenerateOptions options) {
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
		args
	}

	@Override
	void generateHelp() {
		execute(['help', 'generate'], [:])
	}

	@Override
	void configHelp(String language) {
		execute(['config-help', '-g', language], [:])
	}

	private void execute(List<String> args, Map<String, String> systemProperties) {
		log.debug("Executing openapi-generator-cli with args $args and system properties $systemProperties")
		synchronized (lock) {
			// swagger-codegen uses slf4j-simple
			Env.withSystemProperties(configureSlf4jSimple()) {
				// Set system properties for selective generation
				Env.withSystemProperties(systemProperties) {
					// swagger-codegen depends on the context class loader
					Env.withContextClassLoader(clazz.classLoader) {
						clazz.invokeMethod('main', args as String[])
					}
				}
			}
		}
	}

	private static configureSlf4jSimple() {
		// Set log level by the system property
		// https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
		['org.slf4j.simpleLogger.defaultLogLevel': determineLogLevel()]
	}

	private static determineLogLevel() {
		if (log.debugEnabled) {
			return 'DEBUG'
		} else if (log.infoEnabled) {
			return 'INFO'
		} else if (log.warnEnabled) {
			return 'WARN'
		} else {
			return 'ERROR'
		}
	}
}
