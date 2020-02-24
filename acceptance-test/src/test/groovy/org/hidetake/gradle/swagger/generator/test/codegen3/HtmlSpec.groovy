package org.hidetake.gradle.swagger.generator.test.codegen3


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class HtmlSpec extends Specification {

    def project = new GradleProject(':codegen-v3:html')

    def 'plugin should add default tasks into the project'() {
        when:
        def result = project.execute('tasks')

        then:
        result.output.contains('generateSwaggerCode -')
        result.output.contains('generateSwaggerCodeHelp -')
    }

    def 'generateSwaggerCode task should generate HTML'() {
        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-code-petstore/index.html').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerCode')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

    def 'generateSwaggerCodePetstoreHelp task should show help'() {
        when:
        def result = project.execute('generateSwaggerCodePetstoreHelp')

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
