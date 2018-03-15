package org.hidetake.gradle.swagger.generator

import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * A task to generate ReDoc.
 *
 * @author Hidetake Iwata
 */
class GenerateReDoc extends DefaultTask {

    @SkipWhenEmpty @InputFiles
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    boolean wipeOutputDir = true

    @Optional @Input
    String scriptSrc = '//rebilly.github.io/ReDoc/releases/latest/redoc.min.js'

    @Optional @Input
    Map<String, String> options = [:]

    def GenerateReDoc() {
        outputDir = new File(project.buildDir, 'redoc')
    }

    @TaskAction
    void exec() {
        def html = Resources.withInputStream('/redoc.html') { inputStream ->
            new XmlParser(false, false, true).parse(inputStream)
        }
        html.head.first().title.first().value = "ReDoc - ${inputFile.name}"
        html.body.first().redoc.first().attributes().'spec-url' = inputFile.name
        html.body.first().redoc.first().attributes().putAll(options)
        html.body.first().script.first().attributes().src = scriptSrc

        if (wipeOutputDir) {
            assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            project.delete(outputDir)
        }
        outputDir.mkdirs()

        new File(outputDir, 'index.html').withWriter { writer ->
            XmlUtil.serialize(html, writer)
        }
        project.copy {
            from inputFile
            into outputDir
        }
    }

}
