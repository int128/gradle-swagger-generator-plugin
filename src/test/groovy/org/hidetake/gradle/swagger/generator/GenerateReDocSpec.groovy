package org.hidetake.gradle.swagger.generator

import spock.lang.Specification
import spock.lang.Unroll

class GenerateReDocSpec extends Specification {

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def project = Fixture.projectWithPlugin {
            generateReDoc {
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = buildDir
                wipeOutputDir = wipe
            }
        }

        and: 'create a file in the outputDir'
        project.buildDir.mkdirs()
        def keep = new File(project.buildDir, 'keep') << 'something'

        when:
        project.tasks.generateReDoc.exec()

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
            generateReDoc {
                inputFile = Fixture.file(Fixture.YAML.petstore)
                outputDir = projectDir
            }
        }

        when:
        project.tasks.generateReDoc.exec()

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
