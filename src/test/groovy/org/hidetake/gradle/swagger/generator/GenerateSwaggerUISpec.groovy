package org.hidetake.gradle.swagger.generator

import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

class GenerateSwaggerUISpec extends Specification {

    @TempDir
    File repositoryFolder

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
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
        wipe  | verb   | existence
        true  | 'wipe' | false
        false | 'keep' | true
    }

}
