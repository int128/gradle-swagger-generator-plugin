package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

/**
 * An executor of Swagger Codegen V3.
 */
@Slf4j
class V3Executor implements Executor {
    static final String CLASS_NAME = 'io.swagger.codegen.v3.cli.SwaggerCodegen'
    private static final lock = new Object()

    private final Class clazz

    V3Executor(Class clazz) {
        this.clazz = clazz
    }

    @Override
    void generate(GenerateOptions options) {
        execute(V2Executor.buildGenerateArgs(options), options.systemProperties)
    }

    @Override
    void generateHelp() {
        execute(['generate', '-h'], [:])
    }

    @Override
    void configHelp(String language) {
        execute(['config-help', '-l', language], [:])
    }

    private void execute(List<String> args, Map<String, String> systemProperties) {
        log.debug("Executing swagger-codegen-cli with args $args and system properties $systemProperties")
        synchronized (lock) {
            // Set system properties for selective generation
            Env.withSystemProperties(systemProperties) {
                final threadGroup = new ThreadGroup(V3Executor.class.name)
                final thread = new Thread(threadGroup, { ->
                    configureLogback(clazz.classLoader)
                    clazz.invokeMethod('main', args as String[])
                })
                // swagger-codegen depends on the context class loader
                thread.contextClassLoader = clazz.classLoader
                Throwable exception
                thread.uncaughtExceptionHandler = { t, e -> exception = e }
                thread.start()
                thread.join()
                // Wait for child threads spawn by the main of swagger-codegen
                final childThreads = new Thread[128]
                threadGroup.enumerate(childThreads)
                childThreads.each { it?.join() }
                if (exception) {
                    throw new RuntimeException('Error while running swagger-codegen-cli', exception)
                }
            }
        }
    }

    private static void configureLogback(ClassLoader classLoader) {
        final cLoggerFactory = Class.forName('org.slf4j.LoggerFactory', true, classLoader)
        final logger = cLoggerFactory.getLogger('io.swagger')
        if (logger.class.name == 'ch.qos.logback.classic.Logger') {
            final cLevel = Class.forName('ch.qos.logback.classic.Level', true, classLoader)
            final level = determineLogLevel(cLevel)
            logger.setLevel(level)
        }
    }

    private static determineLogLevel(Class cLevel) {
        if (log.debugEnabled) {
            return cLevel.DEBUG
        } else if (log.infoEnabled) {
            return cLevel.INFO
        } else if (log.warnEnabled) {
            return cLevel.WARN
        } else {
            return cLevel.ERROR
        }
    }
}
