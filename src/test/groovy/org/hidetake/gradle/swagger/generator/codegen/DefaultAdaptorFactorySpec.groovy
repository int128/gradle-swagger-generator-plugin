package org.hidetake.gradle.swagger.generator.codegen

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class DefaultAdaptorFactorySpec extends Specification {
    @Unroll
    def "create #expectedAdaptor.simpleName when #givenGenerator is given as generator."() {
        given:
        def project = ProjectBuilder.builder().build()
        project.with {
            repositories {
                jcenter()
            }
            configurations {
                swaggerCodegen
            }
            //TODO: fix slow test
            dependencies {
                swaggerCodegen givenGenerator
            }
        }
        def files = project.configurations.swaggerCodegen.resolve()

        when:
        def adaptor = DefaultAdaptorFactory.instance.findAdaptor(files)

        then:
        adaptor in expectedAdaptor

        where:
        givenGenerator                                    | expectedAdaptor
        'io.swagger:swagger-codegen-cli:2.4.2'            | Swagger2Adaptor
        'io.swagger.codegen.v3:swagger-codegen-cli:3.0.5' | Swagger3Adaptor
        'org.openapitools:openapi-generator-cli:3.3.4'    | OpenAPI3Adaptor
    }

}
