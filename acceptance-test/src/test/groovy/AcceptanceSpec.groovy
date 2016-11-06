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
    def 'generateMyServer task should generate customized server code'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('custom-generator'))
            .withArguments('--stacktrace', '-i', 'generateMyServer')
            .withPluginClasspath()
            .forwardOutput()

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/server/").exists()
    }

    @Unroll
    def 'help tasks should be added into the project'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('simple-generation'))
            .withArguments('--stacktrace', 'tasks')
            .withPluginClasspath()
            .forwardOutput()

        when:
        def result = runner.build()

        then:
        result.output.contains('generateClientHelp')
        result.output.contains('generateServerHelp')
    }

    @Unroll
    def 'generateServerHelp task should show help'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('simple-generation'))
            .withArguments('--stacktrace', 'generateServerHelp')
            .withPluginClasspath()
            .forwardOutput()

        when:
        runner.build()

        then:
        noExceptionThrown()
    }

}
