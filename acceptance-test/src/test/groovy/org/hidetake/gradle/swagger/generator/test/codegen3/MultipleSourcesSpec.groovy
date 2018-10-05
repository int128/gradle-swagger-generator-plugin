package org.hidetake.gradle.swagger.generator.test.codegen3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification
import spock.lang.Unroll

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class MultipleSourcesSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./codegen-v3/multiple-sources'))
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
        result.output.contains('generateSwaggerCodePetstoreV1 -')
        result.output.contains('generateSwaggerCodePetstoreV2 -')
        result.output.contains('generateSwaggerUIPetstoreV1 -')
        result.output.contains('generateSwaggerUIPetstoreV2 -')
        result.output.contains('validateSwaggerPetstoreV1 -')
        result.output.contains('validateSwaggerPetstoreV2 -')
    }

    def 'generateSwaggerCode task should generate code'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerCodePetstoreV1').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstoreV1/src/main/java/example/v1/api/PetsApi.java').exists()
        result.task(':generateSwaggerCodePetstoreV2').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstoreV2/src/main/java/example/v2/api/PetsApi.java').exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerCodePetstoreV1').outcome == TaskOutcome.UP_TO_DATE
        rerunResult.task(':generateSwaggerCodePetstoreV2').outcome == TaskOutcome.UP_TO_DATE
    }

    def 'build task should build the generated code'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'build')

        when:
        runner.build()

        then:
        new File(runner.projectDir, 'build/libs/multiple-sources.jar').exists()
    }

    @Unroll
    def '#taskName task should show help'() {
        given:
        runner.withArguments('--stacktrace', taskName)

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')

        where:
        taskName << ['generateSwaggerCodePetstoreV1Help', 'generateSwaggerCodePetstoreV2Help']
    }

}
