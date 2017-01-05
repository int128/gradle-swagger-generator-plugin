import org.gradle.testkit.runner.GradleRunner

class Fixture {
    static void cleanBuildDir(GradleRunner runner) {
        new File(runner.projectDir, 'build').deleteDir()
    }

    static void placePetstoreYaml(GradleRunner runner, PetstoreYaml petstoreYaml) {
        def buildDir = new File(runner.projectDir, 'build')
        buildDir.mkdirs()
        new File(buildDir, 'petstore.yaml') << Fixture.getResourceAsStream("/petstore-${petstoreYaml}.yaml")
    }

    static enum PetstoreYaml {
        valid, invalid
    }
}
