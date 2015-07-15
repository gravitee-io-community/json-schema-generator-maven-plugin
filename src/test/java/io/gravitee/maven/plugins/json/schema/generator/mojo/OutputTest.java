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

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import io.gravitee.maven.plugins.json.schema.generator.util.Globs;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class OutputTest {

    private static final Globs GLOBS = Mockito.mock(Globs.class);

    private static final Log LOG = Mockito.mock(Log.class);

    private static final JsonSchema SCHEMA = new JsonSchema() {
        @Override
        public JsonFormatTypes getType() {
            return JsonFormatTypes.OBJECT;
        }

        @Override
        public String getId() {
            return "urn:jsonschema:io:gravitee:Clazz";
        }
    };

    private static final String STRING_SCHEMA = "{\n" +
            "  \"type\" : \"object\",\n" +
            "  \"id\" : \"urn:jsonschema:io:gravitee:Clazz\"\n" +
            "}";

    private static Path outputDirectory;

    private static Config config;

    private Output output;

    @BeforeClass
    public static void beforeClass() throws Exception {
        outputDirectory = Files.createTempDirectory("json-schema-generator");
        config = new Config(GLOBS, null, outputDirectory.normalize().toAbsolutePath().toString(), LOG);
    }

    @Before
    public void before() throws Exception {
        output = new Output(config);
    }

    @Test
    public void testWriteWithCorrectSchema() throws Exception {
        output.write(Arrays.asList(SCHEMA));

        Path outputPath = Paths.get(outputDirectory.normalize().toAbsolutePath() + File.separator + SCHEMA.getId() + ".json");
        String actual = new String(Files.readAllBytes(outputPath));

        Assert.assertEquals(STRING_SCHEMA, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteWithNullSchemas() {
        output.write(null);
    }

}