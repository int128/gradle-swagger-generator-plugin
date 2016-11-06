package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * A task to generate an API document.
 *
 * @author Hidetake Iwata
 */
class SwaggerDocgen extends DefaultTask {

    @InputFile
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    Map<String, String> config

    static void injectGenerationTasksFor(SwaggerDocgen task) {
        if (!task.project.tasks.findByName('asciidoctor')) {
            throw new IllegalStateException('''\
Asciidoctor Gradle Plugin should be applied as follows:
  plugins {
    id 'org.asciidoctor.convert' version 'x.x.x'
  }''')
        }

        def cleanTask = task.project.task("${task.name}_clean",
            type: Delete,
            group: 'build',
            description: "Clean up output directory for $task") {
            delete(task.outputDir)
        }

        def markupTask = task.project.task("${task.name}_markup",
            type: SwaggerDocgenMarkup,
            dependsOn: cleanTask,
            group: 'documentation',
            description: "Generate Asciidoc document for $task") {
            inputFile = task.inputFile
            outputDir = task.outputDir
            config = task.config
        }

        def asciidoctorTask = task.project.task("${task.name}_asciidoctor",
            type: task.project.tasks.findByName('asciidoctor').class,
            dependsOn: markupTask,
            group: 'documentation',
            description: "Generate HTML document for $task") {
            sourceDir = task.outputDir
            outputDir = task.outputDir
            separateOutputDirs = false
        }

        task.dependsOn(asciidoctorTask)
    }

}
