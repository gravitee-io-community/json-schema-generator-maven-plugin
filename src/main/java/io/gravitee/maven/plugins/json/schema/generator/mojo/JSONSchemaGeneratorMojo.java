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

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import io.gravitee.maven.plugins.json.schema.generator.util.Globs;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.List;

/**
 * The <code>generate-json-schemas</code> Mojo goal.
 * <p/>
 * This Mojo goal generates JSON Schemas based on its configuration.
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
@Mojo(
        name = "generate-json-schemas",
        requiresDependencyResolution = ResolutionScope.COMPILE,
        configurator = "include-project-dependencies"
)
@SuppressWarnings("unused")
public class JSONSchemaGeneratorMojo extends AbstractMojo {

    /**
     * The list of globs used to include classes for the generation
     */
    @Parameter(property = "includes")
    private List<String> includes;

    /**
     * The list of globs used to exclude classes for the generation
     */
    @Parameter(property = "excludes")
    private List<String> excludes;

    /**
     * The path to the underlying project build path where to find classes
     */
    @Parameter(property = "buildDirectory", defaultValue = "${project.build.outputDirectory}")
    private String buildDirectory;

    /**
     * The JSON Schema generation output directory
     */
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.outputDirectory}/schemas")
    private String outputDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // First, create configuration based on Mojo parameters
        Config config = new Config(
                new Globs(getIncludes(), getExcludes()),
                getBuildDirectory(),
                getOutputDirectory(),
                getLog()
        );

        // Then run mapper
        Mapper mapper = new Mapper(config);
        List<JsonSchema> schemas = mapper.generateJsonSchemas();

        // Finally write JSON Schemas to the configured output directory
        Output output = new Output(config);
        output.write(schemas);
    }

    /**
     * @return The list of globs used to include classes for the generation
     */
    public List<String> getIncludes() {
        return includes;
    }

    /**
     * @return The list of globs used to exclude classes for the generation
     */
    public List<String> getExcludes() {
        return excludes;
    }

    /**
     * @return the path to the underlying project build path where to find classes
     */
    public String getBuildDirectory() {
        return buildDirectory;
    }

    /**
     * @return the JSON Schema generation output directory
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

}
