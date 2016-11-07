import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class CustomCodeGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('custom-code-generator'))
            .withPluginClasspath()
            .forwardOutput()
        new File(runner.projectDir, 'build').deleteDir()
    }

    def 'generateSwaggerCode task should generate customized server code'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        runner.build()

        then:
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
