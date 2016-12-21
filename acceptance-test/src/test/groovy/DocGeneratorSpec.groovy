import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class DocGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('doc-generator'))
            .withPluginClasspath()
            .forwardOutput()
    }

    def 'build task should generate an Swagger UI'() {
        given:
        new File(runner.projectDir, 'build').deleteDir()
        runner.withArguments('--stacktrace', 'build')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/swagger-ui/index.html").exists()
    }

}
