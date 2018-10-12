package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Immutable

/**
 * Options for the generate command of swagger-codegen.
 */
@Immutable
class GenerateOptions {
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
