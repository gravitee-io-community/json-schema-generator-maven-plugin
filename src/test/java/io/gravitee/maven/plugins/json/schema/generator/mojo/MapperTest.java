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
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class MapperTest {

    private static final Log LOG = Mockito.mock(Log.class);

    private static final String BUILD_DIRECTORY = new File(MapperTest.class.getResource("/").getPath()).getAbsolutePath();

    private Mapper mapper;

    @BeforeClass
    public static void beforeClass() {
        Mockito.doThrow(new IllegalStateException("A warning log has been called")).when(LOG).warn(Mockito.anyString(), Mockito.any(Throwable.class));
    }

    @Test
    public void testGenerateJsonSchemasWithSimpleBean() throws Exception {
        mapper = new Mapper(new Config(new Globs(Arrays.asList("SimpleBean.class"), null), BUILD_DIRECTORY, null, LOG));

        List<JsonSchema> schemas = mapper.generateJsonSchemas();
        Assert.assertEquals(1, schemas.size());

        JsonSchema schema = schemas.get(0);
        Assert.assertEquals(
                "SimpleBean",
                schema.getId().substring(schema.getId().lastIndexOf(":") + 1, schema.getId().length())
        );

        Map<String, JsonSchema> properties = schema.asObjectSchema().getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals(JsonFormatTypes.STRING, properties.get("stringParam").getType());
        Assert.assertEquals(JsonFormatTypes.INTEGER, properties.get("integerParam").getType());
    }

    @Test
    public void testGenerateJsonSchemasWithNoBean() throws Exception {
        mapper = new Mapper(new Config(new Globs(Arrays.asList("NoBean.class"), null), BUILD_DIRECTORY, null, LOG));

        List<JsonSchema> schemas = mapper.generateJsonSchemas();
        Assert.assertTrue(schemas.isEmpty());
    }

    @Test
    public void testGenerateJsonSchemasWithRecursive() throws Exception {
        mapper = new Mapper(new Config(new Globs(Arrays.asList("Recursive.class"), null), BUILD_DIRECTORY, null, LOG));

        List<JsonSchema> schemas = mapper.generateJsonSchemas();
        Assert.assertFalse(schemas.isEmpty());
    }

    @Test
    public void testGenerateJsonSchemasWithBeanByUsingExternalDependency() throws Exception {
        mapper = new Mapper(new Config(new Globs(Arrays.asList("BeanWithExternalDependency.class"), null), BUILD_DIRECTORY, null, LOG));

        List<JsonSchema> schemas = mapper.generateJsonSchemas();
        Assert.assertEquals(1, schemas.size());

        JsonSchema schema = schemas.get(0);
        Assert.assertEquals("BeanWithExternalDependency", schema.getId().substring(schema.getId().lastIndexOf(":") + 1, schema.getId().length()));

        Map<String, JsonSchema> properties = schema.asObjectSchema().getProperties();
        Assert.assertEquals(1, properties.size());

        JsonSchema stringSchema = properties.get("jsonFormatTypes");
        Assert.assertEquals(JsonFormatTypes.STRING, stringSchema.getType());
        Assert.assertEquals(new HashSet<>(Arrays.asList("string", "number", "integer",
                "boolean", "object", "array", "null", "any")), stringSchema.asValueTypeSchema().getEnums());
    }


}