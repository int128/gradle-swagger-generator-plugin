package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Immutable

@Immutable
class ConfigHelpOptions {
    Set<File> generatorFiles
    String language
}
