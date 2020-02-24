package org.hidetake.gradle.swagger.generator.test.misc

import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SKIPPED

class DefaultTasksSpec extends Specification {

    def project = new GradleProject(':blank-project')

    @Unroll
    def 'task #taskName should be #status if no input is given'() {
        when:
        final result = project.execute(taskName)

        then:
        result.task(project.absolutePath(taskName)).outcome == status

        where:
        taskName                  | status
        'generateSwaggerCode'     | NO_SOURCE
        'generateSwaggerCodeHelp' | SKIPPED
        'generateSwaggerUI'       | NO_SOURCE
        'validateSwagger'         | NO_SOURCE
    }

}
