import org.gradle.testkit.runner.GradleRunner

class Fixture {
    static void cleanBuildDir(GradleRunner runner) {
        new File(runner.projectDir, 'build').deleteDir()
    }

    static void setupFixture(GradleRunner runner, YAML yaml) {
        def buildDir = new File(runner.projectDir, 'build')
        buildDir.mkdirs()
        new File(buildDir, 'petstore.yaml') << Fixture.getResourceAsStream("/${yaml}.yaml")
    }

    static enum YAML {
        petstore,
        petstore_invalid
    }
}
