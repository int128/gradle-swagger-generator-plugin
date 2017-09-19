package org.hidetake.gradle.swagger.generator

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SwaggerSourcesSpec extends Specification {

    def "plugin should add swaggerSources container"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.extensions.findByName('swaggerSources') instanceof NamedDomainObjectContainer
    }

    def "swaggerSources container should have default values"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
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
