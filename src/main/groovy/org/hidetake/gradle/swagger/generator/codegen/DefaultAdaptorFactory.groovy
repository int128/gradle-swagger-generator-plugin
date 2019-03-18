package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

import java.util.zip.ZipFile

/**
 * A default implementation of {@see AdaptorFactory}.
 */
@Slf4j
@Singleton
class DefaultAdaptorFactory implements AdaptorFactory {
    @Override
    Adaptor findAdaptor(Set<File> generatorFiles) {
        if (findClass(Swagger2Adaptor.CLASS_NAME, generatorFiles)) {
            log.info("Found ${Swagger2Adaptor.CLASS_NAME} for Swagger2Adaptor")
            return new Swagger2Adaptor()
        }
        if (findClass(Swagger3Adaptor.CLASS_NAME, generatorFiles)) {
            log.info("Found ${Swagger3Adaptor.CLASS_NAME} for Swagger3Adaptor")
            return new Swagger3Adaptor()
        }
        if (findClass(OpenAPI3Adaptor.CLASS_NAME, generatorFiles)) {
            log.info("Found ${OpenAPI3Adaptor.CLASS_NAME} for OpenAPI3Adaptor")
            return new OpenAPI3Adaptor()
        }
        null
    }

    static boolean findClass(String className, Set<File> generatorFiles) {
        log.debug("Finding class $className from $generatorFiles")
        def classFileName = className.replace('.', '/') + '.class'
        Helper.findJARs(generatorFiles).find { f ->
            if (!f.name.endsWith('.jar')) {
                return false
            }
            log.info("Finding class $className in $f")
            new ZipFile(f).entries().find { e ->
                e.name == classFileName
            }
        }
    }
}
