package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class GenerateReDocSpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateReDoc == GenerateReDoc
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateReDoc')
    }

    def "plugin should have default properties"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.generateReDoc.scriptSrc =~ /\.js$/
    }

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def path = GenerateReDocSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            generateReDoc {
                inputFile = file(path)
                outputDir = buildDir
                wipeOutputDir = wipe
            }
        }

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

    def "exception should be thrown if projectDir is given"() {
        given:
        def path = GenerateReDocSpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateReDoc.inputFile = file(path)
            tasks.generateReDoc.outputDir = projectDir
            tasks.generateReDoc.exec()
        }

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
