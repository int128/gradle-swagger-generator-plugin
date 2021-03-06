package org.hidetake.gradle.swagger.generator

import com.github.fge.jsonschema.main.JsonSchemaFactory
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.hidetake.gradle.swagger.generator.util.Mappers
import org.hidetake.gradle.swagger.generator.util.Resources

@Slf4j
@CacheableTask
class ValidateSwagger extends DefaultTask {

    @SkipWhenEmpty @InputFile @PathSensitive(PathSensitivity.NAME_ONLY)
    File inputFile

    @OutputFile
    File reportFile

    ValidateSwagger() {
        reportFile = new File(project.buildDir, 'swagger-validation.yaml')
    }

    @TaskAction
    void exec() {

        def swaggerNode = Mappers.YAML.readTree(inputFile)
        def openApiVersion = swaggerNode.get("openapi")

        // by default, use OpenApi spec version 2.0
        def schemaFile = '/schema-oas2.json'
        if(openApiVersion != null && openApiVersion.asText().startsWith("3")) {
            schemaFile =  '/schema-oas3.json'
        }

        def schemaNode = Resources.withInputStream(schemaFile) { inputStream ->
            Mappers.JSON.readTree(inputStream)
        }

        def schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaNode)

        def validation = schema.validate(swaggerNode)

        def validationReport = Mappers.YAML.writeValueAsString(
            success: validation.success,
            messages: validation*.asJson()
        )

        reportFile.parentFile.mkdirs()
        reportFile.text = validationReport

        if (validation.success) {
            log.info("Valid Swagger YAML: $inputFile\n$validationReport")
        } else {
            throw new ValidationException("Invalid Swagger YAML: $inputFile\n$validationReport", validation)
        }
    }

}
