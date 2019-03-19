package org.hidetake.gradle.swagger.generator.codegen

/**
 * {@see AdaptorFactory} creates an instance of {@see Adaptor}.
 */
interface AdaptorFactory {
    /**
     * Create an {@Adaptor} for given generator files.
     *
     * @param generatorFiles
     * @return an instance or {@code null} if a proper adaptor did not find
     */
    Adaptor findAdaptor(Set<File> generatorFiles)
}
