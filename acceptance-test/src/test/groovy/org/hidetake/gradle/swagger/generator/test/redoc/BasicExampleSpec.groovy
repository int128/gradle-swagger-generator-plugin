package org.hidetake.gradle.swagger.generator.test.redoc

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
            .withProjectDir(new File('./redoc'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateReDoc task should generate index.html'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateReDoc')

        when:
        def result = runner.build()

        then:
        result.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateReDocPetstore').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/redoc-petstore/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateReDocPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

}
