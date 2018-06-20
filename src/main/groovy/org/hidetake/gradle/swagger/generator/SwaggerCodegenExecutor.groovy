package org.hidetake.gradle.swagger.generator

import groovy.util.logging.Slf4j
import org.gradle.api.Project

import java.util.concurrent.ConcurrentHashMap

/**
 * An executor class for Swagger Codegen.
 *
 * @author Hidetake Iwata
 */
@Slf4j
class SwaggerCodegenExecutor {

    static final Object lock = new Object()

    private static final CLASS_NAME = 'io.swagger.codegen.SwaggerCodegen'

    private static final CLASS_CACHE = new ConcurrentHashMap<URL[], Class>()

    /**
     * Get the instance for the project.
     *
     * @param project the project
     * @return an instance
     */
    static SwaggerCodegenExecutor getInstance(Project project) {
        new SwaggerCodegenExecutor(findClass(project))
    }

    /**
     * Find Swagger Codegen class from the build script classpath or the swaggerCodegen configuration.
     * If the build script classpath has the class, this just returns it.
     * If the swaggerCodegen configuration has the class, this will load it by a new classloader.
     * Otherwise {@link ClassNotFoundException} will be thrown.
     *
     * @param project the project
     * @return Swagger Codegen class
     */
    private static Class findClass(Project project) {
        try {
            log.debug("Finding class $CLASS_NAME from project class loader: $project.buildscript.classLoader")
            Class.forName(CLASS_NAME, true, project.buildscript.classLoader)
        } catch (ClassNotFoundException ignore) {
            def urls = project.configurations.swaggerCodegen.resolve()*.toURI()*.toURL() as URL[]
            log.debug("Finding class $CLASS_NAME from URLs: $urls")
            CLASS_CACHE.computeIfAbsent(urls) {
                def classLoader = new URLClassLoader(urls)
                try {
                    Class.forName(CLASS_NAME, true, classLoader)
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException('''\
                        Add swagger-codegen-cli to dependencies of the project as follows:
                          dependencies {
                            swaggerCodegen 'io.swagger:swagger-codegen-cli:x.x.x'
                          }'''.stripIndent(), e)
                }
            }
        }
    }

    private final Class swaggerCodegenClass

    /**
     * Constructor.
     *
     * @param swaggerCodegenClass Swagger Codegen class
     * @return an instance
     */
    def SwaggerCodegenExecutor(Class<?> swaggerCodegenClass) {
        this.swaggerCodegenClass = swaggerCodegenClass
    }

    /**
     * Execute Swagger Codegen main.
     *
     * @param systemProperties
     * @param args
     */
    void execute(Map<String, String> systemProperties = null, List<String> args) {
        synchronized (lock) {
            // set log level for slf4j-simple
            // https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
            System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', determineLogLevel())
            // for selective generation
            systemProperties?.each { k, v -> System.setProperty(k, v) }
            // swagger-codegen depends on the context class loader
            def originalContextClassLoader = Thread.currentThread().contextClassLoader
            Thread.currentThread().contextClassLoader = swaggerCodegenClass.classLoader
            try {
                swaggerCodegenClass.invokeMethod('main', args as String[])
            } finally {
                Thread.currentThread().contextClassLoader = originalContextClassLoader
                systemProperties?.each { k, v -> System.clearProperty(k) }
                System.clearProperty('org.slf4j.simpleLogger.defaultLogLevel')
            }
        }
    }

    private static String determineLogLevel() {
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