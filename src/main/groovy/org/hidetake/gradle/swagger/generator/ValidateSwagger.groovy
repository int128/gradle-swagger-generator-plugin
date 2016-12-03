package org.hidetake.gradle.swagger.generator

import com.fasterxml.jackson.databind.SerializationFeature
import com.github.fge.jsonschema.main.JsonSchemaFactory
import groovy.util.logging.Slf4j
import io.swagger.util.Yaml
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

@Slf4j
class ValidateSwagger extends DefaultTask {

    @InputFile
    File inputFile

    @TaskAction
    void exec() {
        def jsonNode = Yaml.mapper().readTree(inputFile)
        def schemaFactory = JsonSchemaFactory.byDefault()
        def schemaResource = ValidateSwagger.getResource('/schema.json')
        def schema = schemaFactory.getJsonSchema(schemaResource?.toString() ?: 'http://swagger.io/v2/schema.json')
        def report = schema.validate(jsonNode)
        def cause = Yaml.mapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsString(report*.asJson())
        if (report.success) {
            log.info("Valid Swagger YAML: $inputFile\n$cause")
        } else {
            throw new ValidationException("Invalid Swagger YAML: $inputFile\n$cause", report)
        }
    }

}
