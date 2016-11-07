import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Unroll

class CodeGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('code-generator'))
            .withPluginClasspath()
            .forwardOutput()
        new File(runner.projectDir, 'build').deleteDir()
    }

    def 'generateServer task should generate server code'() {
        given:
        runner.withArguments('--stacktrace', 'generateServer')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/server/").exists()
    }

    def 'generateClient task should generate server code'() {
        given:
        runner.withArguments('--stacktrace', 'generateClient')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/generated/client/").exists()
    }

    def 'help tasks should be added into the project'() {
        given:
        runner.withArguments('--stacktrace', 'tasks')

        when:
        def result = runner.build()

        then:
        result.output.contains('generateClientHelp')
        result.output.contains('generateServerHelp')
    }

    @Unroll
    def 'task #taskName should show help'() {
        given:
        runner.withArguments('--stacktrace', taskName)

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')

        where:
        taskName << ['generateServerHelp', 'generateClientHelp']
    }

}
