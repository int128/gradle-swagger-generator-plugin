package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical

/**
 * A set of options for generate command of codegen.
 */
@Canonical
class GenerateOptions {
    Set<File> generatorFiles
    String inputFile
    String language
    String outputDir
    String library
    String configFile
    String templateDir
    Map<String, String> additionalProperties
    List<String> rawOptions
    Map<String, String> systemProperties
}
