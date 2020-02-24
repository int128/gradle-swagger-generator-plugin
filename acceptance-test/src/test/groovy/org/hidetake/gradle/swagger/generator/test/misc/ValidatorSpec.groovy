package org.hidetake.gradle.swagger.generator.test.misc

import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.hidetake.gradle.swagger.generator.test.GradleProject
import spock.lang.Specification

class ValidatorSpec extends Specification {

    def project = new GradleProject(':validator')

    def 'validateSwagger task should validate YAML'() {
        when:
        def result = project.execute('validateSwaggerPetstore')

        then:
        result.task(project.absolutePath('validateSwaggerPetstore')).outcome == TaskOutcome.SUCCESS
        project.file('build/swagger-validation-petstore.yaml').exists()

        when:
        def rerunResult = project.executeWithoutClean('validateSwaggerPetstore')

        then:
        rerunResult.task(project.absolutePath('validateSwaggerPetstore')).outcome == TaskOutcome.UP_TO_DATE
    }

    def 'validateSwagger task should fail if YAML is wrong'() {
        when:
        project.execute('validateSwaggerInvalidPetstore')

        then:
        project.file('build/swagger-validation-invalidPetstore.yaml').exists()
        thrown(UnexpectedBuildFailure)
    }

}
