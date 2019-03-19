package org.hidetake.gradle.swagger.generator.codegen

import groovy.transform.Canonical

/**
 * A set of options for {@see org.gradle.api.Project#javaexec(groovy.lang.Closure)}.
 */
@Canonical
class JavaExecOptions {
    Set<File> classpath
    String main
    List<String> args
    Map<String, String> systemProperties
}
