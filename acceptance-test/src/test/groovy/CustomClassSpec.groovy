import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class CustomClassSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('custom-class'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerCode task should generate customized server code'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerCode').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code/src/main/java/example/api/PetsApi.java').exists()
    }

    def 'generateSwaggerCodeHelp task should show help'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCodeHelp')

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
