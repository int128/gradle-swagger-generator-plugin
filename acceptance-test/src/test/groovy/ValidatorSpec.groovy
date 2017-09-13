import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class ValidatorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('validator'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'validateSwagger task should success'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('validateSwagger')

        when:
        def result = runner.build()

        then:
        result.task(':validateSwagger').outcome == TaskOutcome.NO_SOURCE
        result.task(':validateSwaggerPetstore').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/swagger-validation-petstore.yaml").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':validateSwagger').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':validateSwaggerPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

    def 'validateSwagger task should fail due to YAML error'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.invalid)
        runner.withArguments('validateSwagger')

        when:
        runner.build()

        then:
        new File("${runner.projectDir}/build/swagger-validation-petstore.yaml").exists()
        thrown(UnexpectedBuildFailure)

        when:
        runner.build()

        then:
        thrown(UnexpectedBuildFailure)
    }

}
