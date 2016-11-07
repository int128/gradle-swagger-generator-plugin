import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class CodeGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('code-generator'))
            .withPluginClasspath()
            .forwardOutput()
        new File(runner.projectDir, 'build').deleteDir()
    }

    def 'default tasks should be added into the project'() {
        given:
        runner.withArguments('--stacktrace', 'tasks')

        when:
        def result = runner.build()

        then:
        result.output.contains('generateSwaggerCode -')
        result.output.contains('generateSwaggerCodeHelp -')
    }

    def 'generateSwaggerCode task should generate a code'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        runner.build()

        then:
        new File(runner.projectDir, 'build/swagger-code/src/main/java/example/api/PetsApi.java').exists()
    }

    def 'build task should build the generated code'() {
        given:
        runner.withArguments('--stacktrace', 'build')

        when:
        runner.build()

        then:
        new File(runner.projectDir, 'build/libs/code-generator.jar').exists()
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
