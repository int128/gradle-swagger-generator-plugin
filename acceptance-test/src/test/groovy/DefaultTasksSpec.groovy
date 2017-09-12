import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SKIPPED

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
        result.tasks.find { it.path == ":$taskName" }.outcome == status

        where:
        taskName                    | status
        'generateSwaggerCode'       | NO_SOURCE
        'generateSwaggerCodeHelp'   | SKIPPED
        'generateSwaggerUI'         | NO_SOURCE
        'validateSwagger'           | NO_SOURCE
    }

}
