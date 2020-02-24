package org.hidetake.gradle.swagger.generator.test.ui2


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class BasicExampleSpec extends Specification {

    def project = new GradleProject(':ui-v2:basic')

    def 'generateSwaggerUI task should generate index.html'() {
        when:
        def result = project.execute('generateSwaggerUI')

        then:
        result.task(project.absolutePath('generateSwaggerUI')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerUIPetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-ui-petstore/index.html').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerUI')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerUI')).outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(project.absolutePath('generateSwaggerUIPetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

}
