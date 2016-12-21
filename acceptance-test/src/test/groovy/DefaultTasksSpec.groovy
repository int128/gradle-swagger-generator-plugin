import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.Unroll

class DefaultTasksSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('blank-project'))
            .withPluginClasspath()
            .forwardOutput()
    }

    @Unroll
    def 'task #taskName should be skipped'() {
        given:
        runner.withArguments(taskName)

        when:
        def result = runner.build()

        then:
        result.tasks.head().outcome == TaskOutcome.SKIPPED

        where:
        taskName << [
            'generateSwaggerCode',
            'generateSwaggerCodeHelp',
            'generateSwaggerUI',
            'validateSwagger',
        ]
    }

}
