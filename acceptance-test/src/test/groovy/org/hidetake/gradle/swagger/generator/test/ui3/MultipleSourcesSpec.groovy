package org.hidetake.gradle.swagger.generator.test.ui3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir

class MultipleSourcesSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./ui-v3/multiple-sources'))
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
        result.output.contains('generateSwaggerUIPetstoreV1 -')
        result.output.contains('generateSwaggerUIPetstoreV2 -')
    }

    def 'generateSwaggerUI task should generate Swagger UI'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerUI')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerUIPetstoreV1').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/docs/v1/index.html').exists()
        result.task(':generateSwaggerUIPetstoreV2').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/docs/v2/index.html').exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerUIPetstoreV1').outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(':generateSwaggerUIPetstoreV2').outcome == TaskOutcome.UP_TO_DATE
    }

}
