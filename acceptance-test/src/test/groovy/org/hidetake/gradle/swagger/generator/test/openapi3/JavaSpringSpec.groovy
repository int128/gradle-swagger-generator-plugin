package org.hidetake.gradle.swagger.generator.test.openapi3


import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class JavaSpringSpec extends Specification {

    def project = new GradleProject(':openapi-v3:java-spring')

    def 'plugin should add default tasks into the project'() {
        when:
        def result = project.execute('tasks')

        then:
        result.output.contains('generateSwaggerCode -')
        result.output.contains('generateSwaggerCodeHelp -')
    }

    def 'generateSwaggerCode task should generate code'() {
        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-code-petstore/src/main/java/example/api/PetsApi.java').exists()

        when:
        def rerunResult = project.executeWithoutClean('generateSwaggerCode')

        then:
        rerunResult.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

    def 'build task should generate and build code'() {
        when:
        project.execute('build')

        then:
        project.file('build/libs/java-spring.jar').exists()
    }

    def 'generateSwaggerCodePetstoreHelp task should show help'() {
        when:
        def result = project.execute('generateSwaggerCodePetstoreHelp')

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
