import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import spock.lang.Specification

class ValidatorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('validator'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'validateSwagger task should fail due to YAML error'() {
        given:
        runner.withArguments('validateSwagger')

        when:
        runner.build()

        then:
        thrown(UnexpectedBuildFailure)
    }

}
