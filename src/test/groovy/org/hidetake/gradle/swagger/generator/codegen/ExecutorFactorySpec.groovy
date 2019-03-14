package org.hidetake.gradle.swagger.generator.codegen


import spock.lang.Specification
import spock.lang.Unroll
import org.hidetake.gradle.swagger.generator.Fixture

class ExecutorFactorySpec extends Specification {
	@Unroll
	def "create #expectedExecutor.simpleName when #givenGenerator is given as generator." () {
		expect:
		def project = Fixture.projectWithPlugin {
			repositories {
				jcenter()
			}
			dependencies {
				swaggerCodegen givenGenerator
			}
			generateSwaggerCode {
				language = 'java'
				inputFile = Fixture.file(Fixture.YAML.petstore)
				outputDir = buildDir
			}
		}
		def urls = project.tasks.generateSwaggerCode.findConfiguration().resolve()*.toURI()*.toURL() as URL[]
		def executor = ExecutorFactory.instance.getExecutor(project.buildscript.classLoader, urls)
		executor in expectedExecutor
		
		where:
		givenGenerator | expectedExecutor
		'io.swagger:swagger-codegen-cli:2.3.1' | V2Executor
		'io.swagger.codegen.v3:swagger-codegen-cli:3.0.0' | V3Executor
		'org.openapitools:openapi-generator-cli:3.3.4' | OpenApiExecutor
	}

}