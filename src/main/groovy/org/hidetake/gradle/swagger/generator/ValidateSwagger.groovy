package org.hidetake.gradle.swagger.generator

import com.fasterxml.jackson.databind.SerializationFeature
import com.github.fge.jsonschema.main.JsonSchemaFactory
import groovy.util.logging.Slf4j
import io.swagger.util.Json
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
        def schemaResource = ValidateSwagger.getResourceAsStream('/schema.json')
        assert schemaResource, 'JSON schema should be exist in resource'

        def schemaNode = Json.mapper().readTree(schemaResource)
        def schemaFactory = JsonSchemaFactory.byDefault()
        def schema = schemaFactory.getJsonSchema(schemaNode)

        def jsonNode = Yaml.mapper().readTree(inputFile)
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
