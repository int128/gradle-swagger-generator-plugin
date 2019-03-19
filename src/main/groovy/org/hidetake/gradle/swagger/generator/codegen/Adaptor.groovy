package org.hidetake.gradle.swagger.generator.codegen

/**
 * {@see Adaptor} provides conversion between generator options and {@see JavaExecOptions}.
 */
interface Adaptor {
    /**
     * Create options for generate command of codegen.
     *
     * @param options options for generate
     * @return
     */
    JavaExecOptions generate(GenerateOptions options)

    /**
     * Create options for help command of codegen.
     *
     * @param options options for help
     * @return
     */
    JavaExecOptions help(HelpOptions options)

    /**
     * Create options for config-help command of codegen.
     *
     * @param options options for config-help
     * @return
     */
    JavaExecOptions configHelp(ConfigHelpOptions options)
}
