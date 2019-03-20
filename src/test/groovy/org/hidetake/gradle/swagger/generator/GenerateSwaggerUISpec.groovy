package org.hidetake.gradle.swagger.generator

import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerUISpec extends Specification {

    def "task should fail if swaggerUI dependency is not set"() {
        given:
        def project = Fixture.projectWithPlugin()

        when:
        project.tasks.generateSwaggerUI.exec()

        then:
        thrown(IllegalStateException)
    }

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def project = Fixture.projectWithPlugin {
            repositories {
                jcenter()
            }
            //TODO: fix slow test
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:2.2.10'
            }
            generateSwaggerUI {
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = buildDir
                wipeOutputDir = wipe
            }
        }

        and: 'create a file in the outputDir'
        project.buildDir.mkdirs()
        def keep = new File(project.buildDir, 'keep') << 'something'

        when:
        project.tasks.generateSwaggerUI.exec()

        then:
        keep.exists() == existence

        where:
        wipe    | verb      | existence
        true    | 'wipe'    | false
        false   | 'keep'    | true
    }

    def "task should fail if outputDir == projectDir"() {
        given:
        def project = Fixture.projectWithPlugin {
            repositories {
                jcenter()
            }
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:2.2.10'
            }
            generateSwaggerUI {
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = projectDir
            }
        }

        when:
        project.tasks.generateSwaggerUI.exec()

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
