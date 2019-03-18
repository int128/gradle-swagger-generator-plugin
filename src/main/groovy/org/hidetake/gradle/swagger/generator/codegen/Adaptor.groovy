package org.hidetake.gradle.swagger.generator.codegen

interface Adaptor {
    JavaExecOptions generate(GenerateOptions options)
    JavaExecOptions help(HelpOptions options)
    JavaExecOptions configHelp(ConfigHelpOptions options)
}
