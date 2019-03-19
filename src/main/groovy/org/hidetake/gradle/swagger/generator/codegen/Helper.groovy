package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

@Slf4j
class Helper {
    static Set<File> findJARs(Set<File> generatorFiles) {
        generatorFiles.findAll {
            it.name.toLowerCase().endsWith('.jar')
        }
    }

    /**
     * Generates system properties for SLF4J simple.
     * See https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
     *
     * @return system properties with {@code org.slf4j.simpleLogger.defaultLogLevel}
     */
    static Map<String, String> slf4jSimpleSystemProperties() {
        ['org.slf4j.simpleLogger.defaultLogLevel': determineLogLevel()]
    }

    /**
     * Generates system properties for Logback.
     * See https://logback.qos.ch/manual/configuration.html
     *
     * This writes Logback config to a temporary file.
     * It will be deleted on JVM exit.
     *
     * @return system properties with {@code logback.configurationFile}
     */
    static Map<String, String> logbackSystemProperties() {
        ['logback.configurationFile': logbackXmlFile]
    }

    /**
     * Path to the {@code logback.xml}.
     * This is singleton and lazy initialized.
     */
    @Lazy
    static String logbackXmlFile = {
        def f = File.createTempFile('logback-', '.xml')
        f.deleteOnExit()
        f.text = """\
            <configuration>
              <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
                <encoder>
                  <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
                </encoder>
              </appender>
              <root level="${determineLogLevel()}">
                <appender-ref ref="STDOUT" />
              </root>
            </configuration>
            """.stripIndent()
        log.info("Created a temporary file for Logback config: ${f.path}")
        f.path
    }()

    static determineLogLevel() {
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
