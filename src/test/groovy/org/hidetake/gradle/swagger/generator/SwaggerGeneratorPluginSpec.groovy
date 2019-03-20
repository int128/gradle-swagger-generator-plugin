package org.hidetake.gradle.swagger.generator

import org.gradle.api.NamedDomainObjectContainer
import spock.lang.Specification

class SwaggerGeneratorPluginSpec extends Specification {

    def "Task classes should be available in a build script"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.GenerateSwaggerCode == GenerateSwaggerCode
        project.GenerateSwaggerUI == GenerateSwaggerUI
        project.GenerateReDoc == GenerateReDoc
        project.ValidateSwagger == ValidateSwagger
    }

    def "plugin should add default tasks"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.tasks.generateSwaggerCode
        project.tasks.generateSwaggerUI
        project.tasks.generateReDoc
        project.tasks.validateSwagger
        project.tasks.resolveSwaggerTemplate

        and: 'should have default properties'
        project.tasks.generateReDoc.scriptSrc =~ /\.js$/
    }

    def "plugin should add swaggerSources container"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.swaggerSources instanceof NamedDomainObjectContainer
    }

    def "swaggerSources container should have default values"() {
        when:
        def project = Fixture.projectWithPlugin {
            swaggerSources {
                foo {
                    inputFile = file('foo.yaml')
                }
            }
        }

        then:
        project.swaggerSources.foo.code.outputDir == new File(project.buildDir, 'swagger-code-foo')
        project.swaggerSources.foo.ui.outputDir == new File(project.buildDir, 'swagger-ui-foo')
    }

}
