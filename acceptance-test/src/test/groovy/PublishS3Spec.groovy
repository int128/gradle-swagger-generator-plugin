import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class PublishS3Spec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('publish-s3'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'publish task should publish the API client'() {
        given:
        new File(runner.projectDir, 'build').deleteDir()
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
