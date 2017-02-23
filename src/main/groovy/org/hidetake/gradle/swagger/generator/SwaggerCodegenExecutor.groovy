package org.hidetake.gradle.swagger.generator

import org.gradle.api.Project

import java.util.concurrent.ConcurrentHashMap

/**
 * An executor class for Swagger Codegen.
 *
 * @author Hidetake Iwata
 */
class SwaggerCodegenExecutor {
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
     * Find Swagger Codegen class from build script dependencies or configuration.
     *
     * @param project the project
     * @return Swagger Codegen class
     */
    private static findClass(Project project) {
        try {
            Class.forName(CLASS_NAME, true, project.buildscript.classLoader)
        } catch (ClassNotFoundException ignore) {
            def urls = project.configurations.swaggerCodegen.collect { jar -> jar.toURI().toURL() } as URL[]
            CLASS_CACHE.computeIfAbsent(urls) {
                def classLoader = new URLClassLoader(urls, project.buildscript.classLoader)
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
        systemProperties?.each { k, v -> System.setProperty(k, v) }
        def originalContextClassLoader = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = swaggerCodegenClass.classLoader
        try {
            swaggerCodegenClass.invokeMethod('main', args as String[])
        } finally {
            Thread.currentThread().contextClassLoader = originalContextClassLoader
            systemProperties?.each { k, v -> System.clearProperty(k) }
        }
    }
}
