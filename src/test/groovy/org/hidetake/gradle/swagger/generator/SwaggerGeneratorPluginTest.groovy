package org.hidetake.gradle.swagger.generator

import spock.lang.Specification

class SwaggerGeneratorPluginTest extends Specification {

    def "GenerateSwaggerCode class should be available in a build script"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
    }

    def "plugin should add default task"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.tasks.findByName('generateSwaggerCode')
    }

}
