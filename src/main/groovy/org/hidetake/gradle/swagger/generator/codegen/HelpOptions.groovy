package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical

/**
 * A set of options for help command of codegen.
 */
@Canonical
class HelpOptions {
    Set<File> generatorFiles
}
