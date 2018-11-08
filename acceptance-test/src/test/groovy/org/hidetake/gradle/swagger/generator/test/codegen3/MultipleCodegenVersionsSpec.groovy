package org.hidetake.gradle.swagger.generator.test.codegen3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir

class MultipleCodegenVersionsSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./codegen-v3/multiple-codegen-versions'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'tasks should exist in the project'() {
        given:
        runner.withArguments('--stacktrace', 'tasks')

        when:
        def result = runner.build()

        then:
        result.output.contains('generateSwaggerCodePetstoreV2 -')
        result.output.contains('generateSwaggerCodePetstoreV3 -')
        result.output.contains('generateSwaggerUIPetstoreV2 -')
        result.output.contains('generateSwaggerUIPetstoreV3 -')
        result.output.contains('validateSwaggerPetstoreV2 -')
        result.output.contains('validateSwaggerPetstoreV3 -')
    }

    def 'generateSwaggerCode task should generate code'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerCodePetstoreV2').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstoreV2/src/main/java/example/v2/api/PetsApi.java').exists()
        result.task(':generateSwaggerCodePetstoreV3').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstoreV3/src/main/java/example/v3/api/PetsApi.java').exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerCodePetstoreV2').outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(':generateSwaggerCodePetstoreV3').outcome == TaskOutcome.UP_TO_DATE
    }

}
