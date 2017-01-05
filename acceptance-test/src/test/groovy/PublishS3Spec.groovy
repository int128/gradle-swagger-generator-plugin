import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class PublishS3Spec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('publish-s3'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'publish task should publish the API client'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)

        if (System.getenv('AWS_ACCESS_KEY_ID')) {
            runner.withArguments('--stacktrace', 'publish')
        } else {
            runner.withArguments('--stacktrace', 'build')
        }

        when:
        runner.build()

        then:
        noExceptionThrown()
    }

}
