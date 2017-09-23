package org.hidetake.gradle.swagger.generator

import groovy.transform.ToString

/**
 * A domain model of Swagger source.
 *
 * @author Hidetake Iwata
 */
@ToString(includes = 'name', includePackage = false)
class SwaggerSource {
    final String name

    def SwaggerSource(String name) {
        this.name = name
    }

    GenerateSwaggerCode code

    GenerateSwaggerCode code(@DelegatesTo(GenerateSwaggerCode) Closure closure) {
        code.configure(closure)
        code
    }

    GenerateSwaggerUI ui

    GenerateSwaggerUI ui(@DelegatesTo(GenerateSwaggerUI) Closure closure) {
        ui.configure(closure)
        ui
    }

    GenerateReDoc reDoc

    GenerateReDoc reDoc(@DelegatesTo(GenerateReDoc) Closure closure) {
        reDoc.configure(closure)
        reDoc
    }

    ValidateSwagger validation

    ValidateSwagger validation(@DelegatesTo(ValidateSwagger) Closure closure) {
        validation.configure(closure)
        validation
    }

    void setInputFile(File inputFile) {
        [code, ui, reDoc, validation]*.inputFile = inputFile
    }
}
