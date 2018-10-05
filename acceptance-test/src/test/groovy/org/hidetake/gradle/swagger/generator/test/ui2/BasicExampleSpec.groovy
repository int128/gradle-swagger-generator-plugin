package org.hidetake.gradle.swagger.generator.test.ui2

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class BasicExampleSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./ui-v2/basic-example'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerUI task should generate index.html'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateSwaggerUI')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerUI').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateSwaggerUIPetstore').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/swagger-ui-petstore/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerUI').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateSwaggerUIPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

}
