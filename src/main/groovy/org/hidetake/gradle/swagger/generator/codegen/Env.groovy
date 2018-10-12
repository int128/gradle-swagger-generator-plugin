package org.hidetake.gradle.swagger.generator.codegen

/**
 * A utility class for temporarily changing environment.
 */
class Env {
    /**
     * Change system properties while running the closure.
     * @param props
     * @param closure
     * @return
     */
    static <V> V withSystemProperties(Map<String, String> props, Closure<V> closure) {
        def original = props.collectEntries { k, v ->
            [k, System.getProperty(k)]
        } as Map<String, String>
        props.each { k, v -> System.setProperty(k, v) }
        try {
            closure()
        } finally {
            original.each { k, v ->
                if (v) {
                    System.setProperty(k, v)
                } else {
                    System.clearProperty(k)
                }
            }
        }
    }

    /**
     * Change context class loader of the current thread while running the closure.
     * @param classLoader
     * @param closure
     * @return
     */
    static <V> V withContextClassLoader(ClassLoader classLoader, Closure<V> closure) {
        def original = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = classLoader
        try {
            closure()
        } finally {
            Thread.currentThread().contextClassLoader = original
        }
    }
}
