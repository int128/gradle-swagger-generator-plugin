package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory

/**
 * A task to generate an API document.
 *
 * @author Hidetake Iwata
 */
class GenerateSwaggerDoc extends DefaultTask {

    @InputFile
    File inputFile

    @Optional @OutputDirectory
    File outputDir

    @Optional @Input
    Map<String, String> config

    def GenerateSwaggerDoc() {
        outputDir = new File(project.buildDir, 'swagger-doc')
    }

    static void injectGenerationTasksFor(GenerateSwaggerDoc task) {
        def markupTask = task.project.task("${task.name}_swagger2markup",
            type: Swagger2MarkupTask,
            group: 'documentation',
            description: "Generate Asciidoc for $task") {
            inputFile = task.inputFile
            outputDir = task.outputDir
            config = task.config
        }

        if (task.project.tasks.findByName('asciidoctor')) {
            def asciidoctorTask = task.project.task("${task.name}_asciidoctor",
                type: task.project.tasks.findByName('asciidoctor').class,
                dependsOn: markupTask,
                group: 'documentation',
                description: "Generate HTML for $task") {
                sourceDir = task.outputDir
                outputDir = task.outputDir
                separateOutputDirs = false
            }

            task.dependsOn(asciidoctorTask)
        } else {
            task.dependsOn(markupTask)
        }
    }

}
