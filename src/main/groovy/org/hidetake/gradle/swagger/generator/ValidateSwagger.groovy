package org.hidetake.gradle.swagger.generator

import com.github.fge.jsonschema.main.JsonSchemaFactory
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

@Slf4j
class ValidateSwagger extends DefaultTask {

    @SkipWhenEmpty @InputFiles
    File inputFile

    @OutputFile
    File reportFile

    def ValidateSwagger() {
        reportFile = new File(project.buildDir, 'swagger-validation.yaml')
    }

    @TaskAction
    void exec() {
        def schemaNode = Resources.withInputStream('/schema.json') { inputStream ->
            Mappers.JSON.readTree(inputStream)
        }
        def swaggerNode = Mappers.YAML.readTree(inputFile)

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
