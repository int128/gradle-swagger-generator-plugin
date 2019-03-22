package org.hidetake.gradle.swagger.generator

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerUISpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

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
        def repositoryFolder = temporaryFolder.newFolder()
        Fixture.createJAR(new File(repositoryFolder, 'swagger-ui.jar'), [
            'index.html': 'dummy-content',
        ])

        def project = Fixture.projectWithPlugin {
            repositories {
                flatDir {
                    dirs repositoryFolder
                }
            }
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:'
            }
            generateSwaggerUI {
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = buildDir
                wipeOutputDir = wipe
            }

            buildDir.mkdirs()
            new File(buildDir, 'keep') << 'something'
        }

        when:
        project.tasks.generateSwaggerUI.exec()

        then:
        new File(project.buildDir, 'keep').exists() == existence

        where:
        wipe    | verb      | existence
        true    | 'wipe'    | false
        false   | 'keep'    | true
    }

    def "task should fail if outputDir == projectDir"() {
        given:
        def repositoryFolder = temporaryFolder.newFolder()
        Fixture.createJAR(new File(repositoryFolder, 'swagger-ui.jar'), [
            'index.html': 'dummy-content',
        ])

        def project = Fixture.projectWithPlugin {
            repositories {
                flatDir {
                    dirs repositoryFolder
                }
            }
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:'
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
