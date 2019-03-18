package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

@Slf4j
class Helper {
    static Set<File> findJARs(Set<File> generatorFiles) {
        generatorFiles.findAll {
            it.name.toLowerCase().endsWith('.jar')
        }
    }

    static Map<String, String> slf4jSimpleSystemProperties() {
        // Set log level by the system property
        // https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
        ['org.slf4j.simpleLogger.defaultLogLevel': determineLogLevel()]
    }

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
