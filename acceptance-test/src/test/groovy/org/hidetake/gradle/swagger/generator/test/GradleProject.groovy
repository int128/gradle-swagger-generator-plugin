package org.hidetake.gradle.swagger.generator.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure

class GradleProject {
    final String path

    GradleProject(String path) {
        this.path = path
    }

    BuildResult execute(String taskName) {
        file('build').deleteDir()
        executeInternal(taskName)
    }

    BuildResult executeWithoutClean(String taskName) {
        executeInternal(taskName)
    }

    private BuildResult executeInternal(String taskName) {
        try {
            GradleRunner.create()
                .withProjectDir(new File('projects'))
                .withPluginClasspath()
                .withArguments('-s', '--configuration-cache', "$path:$taskName")
                .build()
        } catch (UnexpectedBuildFailure e) {
            throw new BuildFailureException(e)
        }
    }

    String absolutePath(String name) {
        "$path:$name"
    }

    File file(String name) {
        new File(projectDir, name)
    }

    File getProjectDir() {
        new File("projects/$filePath")
    }

    String getFilePath() {
        path.replace(':', '/')
    }

    static class BuildFailureException extends RuntimeException {
        BuildFailureException(UnexpectedBuildFailure e) {
            super("Gradle build failure:\n${e.buildResult.output}", e)
        }
    }
}
