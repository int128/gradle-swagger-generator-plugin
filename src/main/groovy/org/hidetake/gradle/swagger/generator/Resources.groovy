package org.hidetake.gradle.swagger.generator

class Resources {
    static <V> V withInputStream(String path, Closure<V> closure) {
        def stream = Resources.getResourceAsStream(path)
        assert stream, "$path should exist in the resources"
        stream.withStream(closure)
    }
}
