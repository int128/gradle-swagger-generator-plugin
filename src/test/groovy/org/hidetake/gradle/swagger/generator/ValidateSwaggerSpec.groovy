package org.hidetake.gradle.swagger.generator

import spock.lang.Specification

class ValidateSwaggerSpec extends Specification {

    def "task should fail if YAML is wrong"() {
        given:
        def project = Fixture.projectWithPlugin {
            validateSwagger {
                inputFile = Fixture.file(Fixture.YAML.petstore_invalid)
            }
        }

        when:
        project.tasks.validateSwagger.exec()

        then:
        thrown(ValidationException)
    }

}
