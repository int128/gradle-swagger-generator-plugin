package org.hidetake.gradle.swagger.generator.test.codegen2

import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class CustomClassSpec extends Specification {

    def project = new GradleProject(':codegen-v2:custom-class')

    def 'generateSwaggerCode task should generate code using custom generator class'() {
        when:
        def result = project.execute('generateSwaggerCode')

        then:
        result.task(project.absolutePath('generateSwaggerCode')).outcome == TaskOutcome.NO_SOURCE
        result.task(project.absolutePath('generateSwaggerCodePetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-code-petstore/src/main/java/example/api/PetsApi.java').exists()
    }

}
