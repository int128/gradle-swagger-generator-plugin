package org.hidetake.gradle.swagger.generator.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

/**
 * Cached {@link ObjectMapper}s for JSON and YAML.
 *
 * @author Hidetake Iwata
 */
class Mappers {
    /**
     * {@link ObjectMapper} for JSON.
     */
    @Lazy
    static ObjectMapper JSON = {
        new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
    }()

    /**
     * {@link ObjectMapper} for YAML.
     */
    @Lazy
    static ObjectMapper YAML = {
        new ObjectMapper(new YAMLFactory())
            .enable(SerializationFeature.INDENT_OUTPUT)
    }()
}
