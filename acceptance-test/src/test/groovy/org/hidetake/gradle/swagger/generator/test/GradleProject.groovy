package org.hidetake.gradle.swagger.generator.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

class GradleProject {
    final String path

    GradleProject(String path) {
        this.path = path
    }

    BuildResult execute(String taskName) {
        file('build').deleteDir()
        executeWithoutClean(taskName)
    }

    BuildResult executeWithoutClean(String taskName) {
        GradleRunner.create()
            .withProjectDir(new File('examples'))
            .withPluginClasspath()
            .forwardOutput()
            .withArguments('-s', "$path:$taskName")
            .build()
    }

    String absolutePath(String name) {
        "$path:$name"
    }

    File file(String name) {
        new File(projectDir, name)
    }

    File getProjectDir() {
        new File("examples/$filePath")
    }

    String getFilePath() {
        path.replace(':', '/')
    }
}
