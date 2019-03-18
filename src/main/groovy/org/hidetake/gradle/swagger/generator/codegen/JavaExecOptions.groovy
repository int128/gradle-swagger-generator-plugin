package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical

@Canonical
class JavaExecOptions {
    Set<File> classpath
    String main
    List<String> args
    Map<String, String> systemProperties
}
