/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.maven.plugins.json.schema.generator.mojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Build the output to display JSON Schema generation result
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
class Output {

    /**
     * The associated Mojo configuration
     */
    private Config config;
    public static final String WINDOWS_PATH_SEPARATOR = "\\";

    /**
     * Create a new Output instance based on the given Mojo configuration
     *
     * @param config the Mojo configuration
     */
    public Output(Config config) {
        this.config = config;
    }

    /**
     * Writes the given JSON Schema list to the configured output
     *
     * @param schemas the JSON Schema list to write to the configured output
     */
    public void write(List<JsonSchema> schemas) {
        Validate.notNull(schemas, "Unable to write null schemas");

        try {
            // Create the output directory if necessary
            Files.createDirectories(Paths.get(config.getOutputDirectory()));
            // Then create JSON files based on the given JSON Schemas
            createJsonFiles(schemas);
        } catch (IOException e) {
            config.getLogger().error("Unable to create output directory " + config.getOutputDirectory(), e);
        }
    }

    /**
     * Create the JSON files associated to the JSON Schemas
     *
     * @param schemas the list of JSON Schemas to write into files
     */
    private void createJsonFiles(List<JsonSchema> schemas) {
        for (JsonSchema schema : schemas) {
            createJsonFile(schema);
        }
    }

    /**
     * Create the JSON file associated to the JSON Schema
     *
     * @param schema the JSON schema to write into file
     */
    private void createJsonFile(JsonSchema schema) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);

            // replace all : with _ this is a reserved character in some file systems
            Path  outputPath = Paths.get(config.getOutputDirectory() + File.separator + schema.getId().replaceAll(":", "_") + ".json");
            Files.write(outputPath, json.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            config.getLogger().info("Created JSON Schema: " + outputPath.normalize().toAbsolutePath().toString());
        } catch (JsonProcessingException e) {
            config.getLogger().warn("Unable to display schema " + schema.getId(), e);
        } catch (Exception e) {
            config.getLogger().warn("Unable to write Json file for schema " + schema.getId(), e);
        }
    }

}