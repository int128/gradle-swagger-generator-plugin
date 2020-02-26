package org.hidetake.gradle.swagger.generator.executor

import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.hidetake.gradle.swagger.generator.codegen.JavaExecOptions

interface JavaExecutor {
    ExecResult execute(Project project, JavaExecOptions o)
}
