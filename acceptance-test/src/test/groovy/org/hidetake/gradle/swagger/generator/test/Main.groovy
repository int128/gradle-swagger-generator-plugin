package org.hidetake.gradle.swagger.generator.test

import org.gradle.testkit.runner.GradleRunner

class Main {
    static void main(String[] args) {
        assert args.length > 0
        GradleRunner.create()
            .withProjectDir(new File('projects'))
            .withPluginClasspath()
            .forwardOutput()
            .withArguments(args)
            .build()
    }
}
