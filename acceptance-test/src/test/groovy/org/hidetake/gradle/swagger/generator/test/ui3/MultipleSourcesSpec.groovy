package org.hidetake.gradle.swagger.generator.test.ui3


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class MultipleSourcesSpec extends Specification {

    def project = new GradleProject(':ui-v3:multiple-sources')

    def 'tasks should exist in the project'() {
        when:
        def result = project.execute('tasks')

        then:
        result.output.contains('generateSwaggerUIPetstoreV2 -')
        result.output.contains('generateSwaggerUIPetstoreV3 -')
    }

    def 'generateSwaggerUI task should generate Swagger UI'() {
        when:
        def result = project.execute('generateSwaggerUI')

        then:
        result.task(project.absolutePath('generateSwaggerUIPetstoreV2')).outcome == TaskOutcome.SUCCESS
        result.task(project.absolutePath('generateSwaggerUIPetstoreV3')).outcome == TaskOutcome.SUCCESS
        project.file('build/docs/v2/index.html').exists()
        project.file('build/docs/v3/index.html').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerUI')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerUIPetstoreV2')).outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(project.absolutePath('generateSwaggerUIPetstoreV3')).outcome == TaskOutcome.UP_TO_DATE
    }

}
