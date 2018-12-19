package org.hidetake.gradle.swagger.generator.test.codegen3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class ExternalizeTemplateSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./codegen-v3/externalize-template'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerCode task should generate code using external template'() {
        given:
        runner.withArguments('--stacktrace', 'templates:publishToMavenLocal')
        runner.build()

        and:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':resolveSwaggerTemplate').outcome == TaskOutcome.SUCCESS
        result.task(':generateSwaggerCode').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateSwaggerCodePetstore').outcome == TaskOutcome.SUCCESS
    }

}
