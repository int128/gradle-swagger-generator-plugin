import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

class DocGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('doc-generator'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'build task should generate an Swagger UI'() {
        given:
        new File(runner.projectDir, 'build').deleteDir()
        runner.withArguments('--stacktrace', 'build')

        when:
        def result = runner.build()

        then:
        result.tasks.first().outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/swagger-ui/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.tasks.first().outcome == TaskOutcome.UP_TO_DATE
    }

}
