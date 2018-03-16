package org.hidetake.gradle.swagger.generator

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

class GenerateSwaggerUISpec extends Specification {

    def "plugin should provide task class"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.GenerateSwaggerUI == GenerateSwaggerUI
    }

    def "plugin should add default task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
        }

        then:
        project.tasks.findByName('generateSwaggerUI')
    }

    def "exception should be thrown if no dependency given"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            tasks.generateSwaggerUI.exec()
        }

        then:
        thrown(IllegalStateException)
    }

    @Unroll
    def 'task should #verb the output directory if wipeOutputDir == #wipe'() {
        given:
        def path = GenerateSwaggerUISpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            repositories {
                jcenter()
            }
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:2.2.10'
            }
            generateSwaggerUI {
                inputFile = file(path)
                outputDir = buildDir
                wipeOutputDir = wipe
            }
        }

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

    def "exception should be thrown if projectDir is given"() {
        given:
        def path = GenerateSwaggerUISpec.getResource('/petstore-invalid.yaml').path
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'org.hidetake.swagger.generator'
            dependencies {
                swaggerUI 'org.webjars:swagger-ui:2.2.10'
            }
            tasks.generateSwaggerUI.inputFile = file(path)
            tasks.generateSwaggerUI.outputDir = projectDir
            tasks.generateSwaggerUI.exec()
        }

        then:
        AssertionError e = thrown()
        e.message.contains('project directory')
    }

}
