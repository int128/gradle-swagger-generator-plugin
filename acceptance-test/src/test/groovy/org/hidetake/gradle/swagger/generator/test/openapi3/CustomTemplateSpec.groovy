package org.hidetake.gradle.swagger.generator.test.openapi3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class CustomTemplateSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./openapi-v3/custom-template'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerCode task should generate customized server code'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerCode').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateSwaggerCodePetstore').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstore/src/main/java/example/api/PetsApi.java').exists()
    }

    def 'generateSwaggerCodePetstoreHelp task should show help'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCodePetstoreHelp')

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
