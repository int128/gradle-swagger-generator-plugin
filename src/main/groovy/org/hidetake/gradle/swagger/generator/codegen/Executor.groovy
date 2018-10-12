package org.hidetake.gradle.swagger.generator.codegen

/**
 * An interface for executing the Swagger Codegen CLI.
 */
interface Executor {
    /**
     * Execute code generation.
     * @param options
     */
    void generate(GenerateOptions options)

    /**
     * Show help for code generation.
     */
    void generateHelp()

    /**
     * Show help of the config.
     * @param language
     */
    void configHelp(String language)
}
