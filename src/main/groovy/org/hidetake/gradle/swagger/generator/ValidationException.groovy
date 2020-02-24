package org.hidetake.gradle.swagger.generator

import com.github.fge.jsonschema.core.report.ProcessingReport

class ValidationException extends RuntimeException {
    final ProcessingReport processingReport
    ValidationException(String message, ProcessingReport processingReport) {
        super(message)
        this.processingReport = processingReport
    }
}
