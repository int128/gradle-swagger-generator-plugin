package org.hidetake.gradle.swagger.generator

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ResolveSwaggerTemplateSpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    def 'task should expand JARs into build/swagger-template'() {
        given:
        def repositoryFolder = temporaryFolder.newFolder()
        Fixture.createJAR(new File(repositoryFolder, 'template-foo-1.0.0.jar'), [
            'foo.txt': 'foo-hello-world',
        ])
        Fixture.createJAR(new File(repositoryFolder, 'template-bar-1.0.0.jar'), [
            'bar.txt': 'bar-hello-world',
        ])

        def project = Fixture.projectWithPlugin {
            repositories {
                flatDir {
                    dirs repositoryFolder
                }
            }
            dependencies {
                swaggerTemplate 'com.example:template-foo:1.0.0'
                swaggerTemplate 'com.example:template-bar:1.0.0'
            }
        }

        when:
        project.tasks.resolveSwaggerTemplate.copy()

        then:
        project.file('build/swagger-template/foo.txt').text == 'foo-hello-world'
        project.file('build/swagger-template/bar.txt').text == 'bar-hello-world'
    }

}
