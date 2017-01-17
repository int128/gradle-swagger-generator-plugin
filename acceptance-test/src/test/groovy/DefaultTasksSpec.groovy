import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.SKIPPED
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class DefaultTasksSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('blank-project'))
            .withPluginClasspath()
            .forwardOutput()
    }

    @Unroll
    def 'task #taskName should be #status if no input is given'() {
        given:
        runner.withArguments(taskName)

        when:
        def result = runner.build()

        then:
        result.tasks.head().outcome == status

        where:
        taskName                    | status
        'generateSwaggerCode'       | UP_TO_DATE
        'generateSwaggerCodeHelp'   | SKIPPED
        'generateSwaggerUI'         | UP_TO_DATE
        'validateSwagger'           | UP_TO_DATE
    }

}
