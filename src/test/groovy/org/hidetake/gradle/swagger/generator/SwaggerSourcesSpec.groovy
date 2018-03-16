package org.hidetake.gradle.swagger.generator

import org.gradle.api.NamedDomainObjectContainer
import spock.lang.Specification

class SwaggerSourcesSpec extends Specification {

    def "plugin should add swaggerSources container"() {
        when:
        def project = Fixture.projectWithPlugin()

        then:
        project.extensions.findByName('swaggerSources') instanceof NamedDomainObjectContainer
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
