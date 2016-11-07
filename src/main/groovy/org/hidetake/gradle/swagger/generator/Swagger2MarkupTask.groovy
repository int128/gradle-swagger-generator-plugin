package org.hidetake.gradle.swagger.generator

import io.github.swagger2markup.Swagger2MarkupConverter
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * An internal task to generate an API document.
 *
 * @author Hidetake Iwata
 */
class Swagger2MarkupTask extends DefaultTask {

    @InputFile
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    Map<String, String> config

    @TaskAction
    void exec() {
        project.delete(outputDir)
        outputDir.mkdirs()

        def swagger2MarkupConfig = new Swagger2MarkupConfigBuilder(config ?: [:]).build()
        Swagger2MarkupConverter
            .from(inputFile.toPath())
            .withConfig(swagger2MarkupConfig)
            .build()
            .toFile(outputDir.toPath().resolve('index'))
    }

}
