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
package io.gravitee.maven.plugins.json.schema.generator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Configured globs used for path matching
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class Globs {
    /**
     * The list of globs used to include during path matching
     */
    private final List<String> includes;

    /**
     * The list of globs used to exclude during path matching
     */
    private final List<String> excludes;

    /**
     * Create a new instance based on the given included/excluded list of globs
     * <p/>
     * If no included or excluded glob list is given (null), then create an empty one.
     *
     * @param includes the included list of globs
     * @param excludes the excluded list of globs
     */
    public Globs(List<String> includes, List<String> excludes) {
        this.includes = includes == null ? new ArrayList<String>() : includes;
        this.excludes = excludes == null ? new ArrayList<String>() : excludes;
    }

    /**
     * @return the list of globs used to include during path matching
     */
    public List<String> getIncludes() {
        return includes;
    }

    /**
     * @return the list of globs used to exclude during path matching
     */
    public List<String> getExcludes() {
        return excludes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Globs globs = (Globs) o;

        if (includes != null ? !includes.equals(globs.includes) : globs.includes != null) return false;
        return !(excludes != null ? !excludes.equals(globs.excludes) : globs.excludes != null);

    }

    @Override
    public int hashCode() {
        int result = includes != null ? includes.hashCode() : 0;
        result = 31 * result + (excludes != null ? excludes.hashCode() : 0);
        return result;
    }
}
