import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Unroll

class AcceptanceSpec extends Specification {

    @Unroll
    def 'generateServer task should generate server code'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('simple-generation'))
            .withArguments('--stacktrace', 'generateServer')
            .withPluginClasspath()
            .forwardOutput()

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/server/").exists()
    }

    @Unroll
    def 'generateClient task should generate server code'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('simple-generation'))
            .withArguments('--stacktrace', 'generateClient')
            .withPluginClasspath()
            .forwardOutput()

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/client/").exists()
    }

    @Unroll
    def 'swaggerCodegenHelp task should show help'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('simple-generation'))
            .withArguments('--stacktrace', 'swaggerCodegenHelp')
            .withPluginClasspath()
            .forwardOutput()

        when:
        runner.build()

        then:
        noExceptionThrown()
    }

}
