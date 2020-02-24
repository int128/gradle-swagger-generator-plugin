package org.hidetake.gradle.swagger.generator.test.codegen3


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class MultipleCodegenVersionsSpec extends Specification {

    def project = new GradleProject(':codegen-v3:multiple-codegen-versions')

    def 'tasks should exist in the project'() {
        when:
        def result = project.execute('tasks')

        then:
        result.output.contains('generateSwaggerCodePetstoreV2 -')
        result.output.contains('generateSwaggerCodePetstoreV3 -')
        result.output.contains('generateSwaggerUIPetstoreV2 -')
        result.output.contains('generateSwaggerUIPetstoreV3 -')
        result.output.contains('validateSwaggerPetstoreV2 -')
        result.output.contains('validateSwaggerPetstoreV3 -')
    }

    def 'generateSwaggerCode task should generate code'() {
        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('generateSwaggerCodePetstoreV2')).outcome == TaskOutcome.SUCCESS
        result.task(project.absolutePath('generateSwaggerCodePetstoreV3')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-code-petstoreV2/src/main/java/example/v2/api/PetsApi.java').exists()
        project.file('build/swagger-code-petstoreV3/src/main/java/example/v3/api/PetsApi.java').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerCode')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstoreV2')).outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstoreV3')).outcome == TaskOutcome.UP_TO_DATE
    }

}
