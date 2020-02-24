package org.hidetake.gradle.swagger.generator

import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.hidetake.gradle.swagger.generator.util.Resources

/**
 * A task to generate ReDoc.
 *
 * @author Hidetake Iwata
 */
@CacheableTask
class GenerateReDoc extends DefaultTask {

    @SkipWhenEmpty @InputFile @PathSensitive(PathSensitivity.NAME_ONLY)
    File inputFile

    @OutputDirectory
    File outputDir

    @Optional @Input
    String title

    @Input
    boolean wipeOutputDir = true

    @Optional @Input
    String scriptSrc = '//rebilly.github.io/ReDoc/releases/latest/redoc.min.js'

    @Optional @Input
    Map<String, String> options = [:]

    GenerateReDoc() {
        outputDir = new File(project.buildDir, 'redoc')
    }

    @TaskAction
    void exec() {
        def html = Resources.withInputStream('/redoc.html') { inputStream ->
            new XmlParser(false, false, true).parse(inputStream)
        }
        if(!html.head.first().title.first().value) {
            if(title == null) {
                title = "ReDoc - ${inputFile.name}"
            }
            html.head.first().title.first().value = title
        }
        html.body.first().redoc.first().attributes().'spec-url' = inputFile.name
        html.body.first().redoc.first().attributes().putAll(options)
        html.body.first().script.first().attributes().src = scriptSrc

        if (wipeOutputDir) {
            assert outputDir != project.projectDir, 'Prevent wiping the project directory'
            project.delete(outputDir)
        }
        outputDir.mkdirs()

        new File(outputDir, 'index.html').withWriter('UTF-8') { writer ->
            XmlUtil.serialize(html, writer)
        }
        project.copy {
            from inputFile
            into outputDir
        }
    }

}
