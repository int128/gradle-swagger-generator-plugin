package org.hidetake.gradle.swagger.generator.test.codegen2

import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification
import spock.lang.Unroll

class MultipleSourcesSpec extends Specification {

    def project = new GradleProject(':codegen-v2:multiple-sources')

    def 'tasks should exist in the project'() {
        when:
        def result = project.execute('tasks')

        then:
        result.output.contains('generateSwaggerCodePetstoreV1 -')
        result.output.contains('generateSwaggerCodePetstoreV2 -')
        result.output.contains('generateSwaggerUIPetstoreV1 -')
        result.output.contains('generateSwaggerUIPetstoreV2 -')
        result.output.contains('validateSwaggerPetstoreV1 -')
        result.output.contains('validateSwaggerPetstoreV2 -')
    }

    def 'generateSwaggerCode task should generate code'() {
        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('generateSwaggerCodePetstoreV1')).outcome == TaskOutcome.SUCCESS
        result.task(project.absolutePath('generateSwaggerCodePetstoreV2')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-code-petstoreV1/src/main/java/example/v1/api/PetsApi.java').exists()
        project.file('build/swagger-code-petstoreV2/src/main/java/example/v2/api/PetsApi.java').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerCode')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstoreV1')).outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstoreV2')).outcome == TaskOutcome.UP_TO_DATE
    }

    @Unroll
    def '#taskName task should show help'() {
        when:
        def result = project.execute(taskName)

        then:
        result.output.contains('CONFIG OPTIONS')

        where:
        taskName << ['generateSwaggerCodePetstoreV1Help', 'generateSwaggerCodePetstoreV2Help']
    }

}
