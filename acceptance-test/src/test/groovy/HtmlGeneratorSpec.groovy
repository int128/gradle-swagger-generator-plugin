import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class HtmlGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('html-generator'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'generateApiDoc task should generate API document'() {
        given:
        runner.withArguments('--stacktrace', 'generateApiDoc')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/docs/index.html").exists()
    }

}
