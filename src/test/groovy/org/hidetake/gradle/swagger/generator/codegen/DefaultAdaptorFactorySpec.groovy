package org.hidetake.gradle.swagger.generator.codegen

import org.gradle.testfixtures.ProjectBuilder
import org.hidetake.gradle.swagger.generator.Fixture
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class DefaultAdaptorFactorySpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Unroll
    def "create #expectedAdaptor.simpleName when class #givenClassFileName is given"() {
        given:
        def repositoryFolder = temporaryFolder.newFolder()
        Fixture.createJAR(new File(repositoryFolder, 'swagger-codegen-cli-1.0.0.jar'), [
            (givenClassFileName)  : 'dummy-class',
            'META-INF/MANIFEST.MF': 'dummy-manifest',
        ])

        def project = ProjectBuilder.builder().build()
        project.with {
            repositories {
                flatDir {
                    dirs repositoryFolder
                }
            }
            configurations {
                swaggerCodegen
            }
            dependencies {
                swaggerCodegen 'com.example:swagger-codegen-cli:1.0.0'
            }
        }
        def files = project.configurations.swaggerCodegen.resolve()

        when:
        def adaptor = DefaultAdaptorFactory.instance.findAdaptor(files)

        then:
        adaptor in expectedAdaptor

        where:
        givenClassFileName                                | expectedAdaptor
        'io/swagger/codegen/SwaggerCodegen.class'         | Swagger2Adaptor
        'io/swagger/codegen/v3/cli/SwaggerCodegen.class'  | Swagger3Adaptor
        'org/openapitools/codegen/OpenAPIGenerator.class' | OpenAPI3Adaptor
    }

}
