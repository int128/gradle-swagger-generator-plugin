import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Unroll

class AcceptanceSpec extends Specification {

    @Unroll
    def 'acceptance test should pass on Gradle #gradleVersion'() {
        given:
        def runner = GradleRunner.create()
            .withProjectDir(new File('fixture'))
            .withArguments('test')
            .withPluginClasspath()
            .withGradleVersion(gradleVersion)

        when:
        runner.build()

        then:
        noExceptionThrown()

        where:
        gradleVersion << [System.getProperty('current.gradle.version'), '2.13']
    }

}
