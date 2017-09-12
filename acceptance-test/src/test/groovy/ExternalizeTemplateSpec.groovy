import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class ExternalizeTemplateSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('externalize-template'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerCode task should generate code using external template'() {
        given:
        runner.withArguments('--stacktrace', 'templates:publishToMavenLocal')
        runner.build()

        and:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.tasks.find { it.path == ':resolveSwaggerTemplate' }.outcome == TaskOutcome.SUCCESS
        result.tasks.find { it.path == ':generateSwaggerCode' }.outcome == TaskOutcome.SUCCESS
    }

}
