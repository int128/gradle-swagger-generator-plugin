import org.gradle.testkit.runner.GradleRunner

class Main {
    static void main(String[] args) {
        assert args.length > 0
        def runner = GradleRunner.create()
            .withProjectDir(new File(args.head()))
            .withPluginClasspath()
            .forwardOutput()
            .withArguments(args.tail())

        Fixture.cleanBuildDir(runner)
        Fixture.setupFixture(runner, Fixture.YAML.petstore)

        runner.build()
    }
}
