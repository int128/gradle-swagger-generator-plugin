import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class CustomCodeGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('custom-code-generator'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'generateMyServer task should generate customized server code'() {
        given:
        runner.withArguments('--stacktrace', 'generateMyServer')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/server/").exists()
    }

    def 'generateMyServerHelp task should show help'() {
        given:
        runner.withArguments('--stacktrace', 'generateMyServerHelp')

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
