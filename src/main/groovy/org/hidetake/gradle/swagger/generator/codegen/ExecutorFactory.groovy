package org.hidetake.gradle.swagger.generator.codegen

import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

@Slf4j
@Singleton
class ExecutorFactory {
    private final classCache = new ConcurrentHashMap<URL[], Class>()

    Executor getExecutor(ClassLoader classLoader, URL[] urls) {
        final c2 = findClass(V2Executor.CLASS_NAME, classLoader, urls)
        if (c2) {
            return new V2Executor(c2)
        }
        final c3 = findClass(V3Executor.CLASS_NAME, classLoader, urls)
        if (c3) {
            return new V3Executor(c3)
        }
        throw new IllegalStateException('''\
            Add swagger-codegen-cli to dependencies of the project as follows:
              dependencies {
                swaggerCodegen 'io.swagger:swagger-codegen-cli:x.x.x'
              }'''.stripIndent())
    }

    private Class findClass(String className, ClassLoader classLoader, URL[] urls) {
        try {
            log.debug("Finding class $className from class loader: $classLoader")
            Class.forName(className, true, classLoader)
        } catch (ClassNotFoundException ignore) {
            log.debug("Finding class $className from URLs: $urls")
            classCache.computeIfAbsent(urls) {
                def urlClassLoader = new URLClassLoader(urls)
                try {
                    Class.forName(className, true, urlClassLoader)
                } catch (ClassNotFoundException ignored) {
                    null
                }
            }
        }
    }
}
