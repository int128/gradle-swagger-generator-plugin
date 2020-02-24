package org.hidetake.gradle.swagger.generator.test.redoc


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class BasicExampleSpec extends Specification {

    def project = new GradleProject(':redoc:basic')

    def 'generateReDoc task should generate index.html'() {
        when:
        def result = project.execute('generateReDoc')

        then:
        result.task(project.absolutePath('generateReDoc')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateReDocPetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/redoc-petstore/index.html').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateReDoc')

        then:
        rerunResult.task(project.absolutePath('generateReDoc')).outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(project.absolutePath('generateReDocPetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

}
