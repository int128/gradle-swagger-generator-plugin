import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class DocGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('doc-generator'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'build task should generate an Swagger UI'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('--stacktrace', 'build')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerUI').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/swagger-ui/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerUI').outcome == TaskOutcome.UP_TO_DATE
    }

}
