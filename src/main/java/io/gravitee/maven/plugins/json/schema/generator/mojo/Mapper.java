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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import io.gravitee.maven.plugins.json.schema.generator.util.ClassFinder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Generates JSON Schemas from the matched Class Paths, by mapping Class attributes to the associated JSON field.
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
class Mapper {

    /**
     * The associated Mojo configuration
     */
    private Config config;

    /**
     * Creates a new Mapper instance based on the given Mojo configuration
     *
     * @param config the Mojo configuration
     */
    public Mapper(Config config) {
        this.config = config;
    }

    /**
     * Generates JSON Schemas from the matched Class names
     *
     * @return a list of JSON Schemas from the matched Class names
     */
    public List<JsonSchema> generateJsonSchemas() {
        final List<JsonSchema> generatedSchemas = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()).setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mapper.registerModule(new Jdk8Module());

        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);

        for (String className : generateClassNames()) {
            try {
                JsonSchema schema = null;
                try {
                    schema = jsonSchemaGenerator.generateSchema(getClass().getClassLoader().loadClass(className));
                    if (schema == null) {
                        throw new IllegalArgumentException("Could not build schema or find any classes.");
                    }
                    generatedSchemas.add(schema);
                } catch (JsonMappingException e) {
                    throw new GenerationException("Unable to format class " + className, e);
                }
            } catch (GenerationException | ClassNotFoundException e) {
                config.getLogger().warn("Unable to generate JSON schema for class " + className, e);
            }
        }
        return generatedSchemas;
    }

    /**
     * Generates Class names from the matched Paths
     *
     * @return a list of Class names from the matched Paths
     */
    private List<String> generateClassNames() {
        Set<String> classNames = new HashSet<>();
        Path root = Paths.get(config.getBuildDirectory());
        if (!root.toFile().isDirectory()) {
            config.getLogger().error("Invalid underlying project build directory: " + config.getBuildDirectory());
            return Collections.<String>emptyList();
        }
        try {
            classNames.addAll(ClassFinder.findClassNames(root, config.getGlobs()));
            config.getLogger().debug("Generated class names: " + classNames);
        } catch (IOException e) {
            config.getLogger().warn("Unable to generate JSON schemas", e);
        }
        return new ArrayList<>(classNames);
    }

    private static class GenerationException extends Exception {

        public GenerationException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
