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

import io.gravitee.maven.plugins.json.schema.generator.util.Globs;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Wrapper class to the Mojo configuration
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
class Config {

    /**
     * Wrapper to configured included/excluded globs
     */
    private final Globs globs;

    /**
     * The path to the underlying project build path where to find classes
     */
    @Parameter(readonly = true, defaultValue = "${project.build.directory}")
    private final String buildDirectory;

    /**
     * The JSON Schema generation output directory
     */
    @Parameter(readonly = true, defaultValue = "${project.build.directory}/schema")
    private final String outputDirectory;

    /**
     * The associated logger to the Mojo
     */
    private final Log logger;

    /**
     * Creates a configuration wrapper based on the given configuration parameters
     *
     * @param globs           the wrapper to configured included/excluded globs
     * @param buildDirectory  the list of classpaths used to find classes to generate
     * @param outputDirectory the directory that will contain JSON Schema generation output
     * @param logger          the associated logger to the Mojo
     */
    public Config(Globs globs, String buildDirectory, String outputDirectory, Log logger) {
        this.globs = globs;
        this.buildDirectory = buildDirectory;
        this.outputDirectory = outputDirectory;
        this.logger = logger;
    }

    /**
     * @return the wrapper to configured included/excluded globs
     */
    public Globs getGlobs() {
        return globs;
    }

    /**
     * @return the path to the underlying project build path where to find classes
     */
    public String getBuildDirectory() {
        return buildDirectory;
    }

    /**
     * @return the directory that will contain JSON Schema generation output
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @return the associated logger to the Mojo
     */
    public Log getLogger() {
        return logger;
    }
}
