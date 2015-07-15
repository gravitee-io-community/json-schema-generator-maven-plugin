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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class GlobsTest {

    private static final List<String> INCLUDES = Arrays.asList(new String[]{"path/to/include/*.class"});

    private static final List<String> EXCLUDES = Arrays.asList(new String[]{"path/to/exclude/*.class"});

    @Test
    public void testGlobsCreationWithNullIncludes() {
        Globs globs = new Globs(null, EXCLUDES);
        Assert.assertEquals(Collections.<String>emptyList(), globs.getIncludes());
        Assert.assertEquals(EXCLUDES, globs.getExcludes());
    }

    @Test
    public void testGlobsCreationWithNullExcludes() {
        Globs globs = new Globs(INCLUDES, null);
        Assert.assertEquals(INCLUDES, globs.getIncludes());
        Assert.assertEquals(Collections.<String>emptyList(), globs.getExcludes());
    }

    @Test
    public void testGlobsCreationWithNullBothIncludesAndExclude() {
        Globs globs = new Globs(null, null);
        Assert.assertEquals(Collections.<String>emptyList(), globs.getIncludes());
        Assert.assertEquals(Collections.<String>emptyList(), globs.getExcludes());
    }

    @Test
    public void testGlobsCreationWithNonNullBothIncludesAndExclude() {
        Globs globs = new Globs(INCLUDES, EXCLUDES);
        Assert.assertEquals(INCLUDES, globs.getIncludes());
        Assert.assertEquals(EXCLUDES, globs.getExcludes());
    }

}