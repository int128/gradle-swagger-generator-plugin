package org.hidetake.gradle.swagger.generator.test.codegen3

import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class ExternalizeClassSpec extends Specification {

    def project = new GradleProject(':codegen-v3:externalize-class')

    def 'generateSwaggerCode task should generate code using external template'() {
        given:
        project.execute('generators:publishToMavenLocal')

        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('resolveSwaggerTemplate')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.SUCCESS
    }

}
