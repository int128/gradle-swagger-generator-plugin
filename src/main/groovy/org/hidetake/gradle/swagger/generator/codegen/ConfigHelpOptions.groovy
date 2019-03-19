package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical

/**
 * A set of options for config-help command of codegen.
 */
@Canonical
class ConfigHelpOptions {
    Set<File> generatorFiles
    String language
}
