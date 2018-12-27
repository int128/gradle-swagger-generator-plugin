package org.hidetake.gradle.swagger.generator.test.redoc

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class CustomOptionExampleSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./redoc/custom-options'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateReDoc task should generate index.html with custom options'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateReDoc')

        when:
        def result = runner.build()

        then:
        result.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateReDocPetstore').outcome == TaskOutcome.SUCCESS
        def htmlFile = new File("${runner.projectDir}/build/redoc-petstore/index.html")
        htmlFile.exists()
        def html = new XmlParser(false, false, true).parse(htmlFile)
        html.body.first().redoc.first().attributes().size() == 2

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateReDocPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

}
