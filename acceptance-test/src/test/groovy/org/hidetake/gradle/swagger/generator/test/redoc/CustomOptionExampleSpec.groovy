package org.hidetake.gradle.swagger.generator.test.redoc


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class CustomOptionExampleSpec extends Specification {

    def project = new GradleProject(':redoc:custom-options')

    def 'generateReDoc task should generate index.html with custom options'() {
        when:
        def result = project.execute('generateReDoc')

        then:
        result.task(project.absolutePath('generateReDoc')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateReDocPetstore')).outcome == TaskOutcome.SUCCESS
        def htmlFile = project.file('build/redoc-petstore/index.html')
        htmlFile.exists()
        def html = new XmlParser(false, false, true).parse(htmlFile)
        html.head.first().title.first().value().first() == 'Custom title'
        html.body.first().redoc.first().attributes().size() == 2

        when:
        def rerunResult = project.executeWithoutClean('generateReDoc')

        then:
        rerunResult.task(project.absolutePath('generateReDoc')).outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(project.absolutePath('generateReDocPetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

}
