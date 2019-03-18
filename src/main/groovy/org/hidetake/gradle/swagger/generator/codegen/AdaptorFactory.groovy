package org.hidetake.gradle.swagger.generator.codegen

interface AdaptorFactory {
    Adaptor findAdaptor(Set<File> generatorFiles)
}
