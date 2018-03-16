package org.hidetake.gradle.swagger.generator

import spock.lang.Specification

class ValidateSwaggerSpec extends Specification {

    def "ValidateSwagger class should be available in a build script"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.ValidateSwagger == ValidateSwagger
    }

    def "plugin should add default task"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.tasks.findByName('validateSwagger')
    }

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
