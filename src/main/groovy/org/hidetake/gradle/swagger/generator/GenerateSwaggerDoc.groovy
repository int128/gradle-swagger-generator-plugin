package org.hidetake.gradle.swagger.generator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

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

    @Optional @Input
    Map<String, String> options

    @Optional @Input
    Map<String, String> attributes

    def GenerateSwaggerDoc() {
        outputDir = new File(project.buildDir, 'swagger-doc')
        attributes = [
            toc: 'right',
            sectnums: '',
            sectanchors: '',
        ]
    }

    static void injectGenerationTasksFor(GenerateSwaggerDoc task) {
        def cleanTask = task.project.task("${task.name}_clean",
            type: Delete,
            group: 'build',
            description: "Deletes the build directory $task") {
            delete(task.outputDir)
            doLast {
                task.outputDir.mkdirs()
            }
        }

        def markupTask = task.project.task("${task.name}_swagger2markup",
            type: Swagger2MarkupTask,
            dependsOn: cleanTask,
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
                options = task.options
                attributes = task.attributes
                separateOutputDirs = false
            }

            task.dependsOn(asciidoctorTask)
        } else {
            task.dependsOn(markupTask)
        }
    }

}
