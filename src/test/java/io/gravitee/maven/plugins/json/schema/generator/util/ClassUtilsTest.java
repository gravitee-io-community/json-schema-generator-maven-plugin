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

/**
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class ClassUtilsTest {

    private static final String PATH = "path/to/class/Class.class";
    private static final String BASE_PATH = "path/to/";

    @Test
    public void testConvertClassPathToClassNamePathWithCorrectPath() throws Exception {
        Assert.assertEquals("path.to.class.Class", ClassUtils.convertClassPathToClassName(PATH));
    }

    @Test(expected = NullPointerException.class)
    public void testConvertClassPathToClassNamePathWithNullPath() throws Exception {
        ClassUtils.convertClassPathToClassName(null);
    }

    @Test
    public void testConvertClassPathToClassNamePathBasePathWithCorrectPathAndBasePath() throws Exception {
        Assert.assertEquals("class.Class", ClassUtils.convertClassPathToClassName(PATH, BASE_PATH));
    }

    @Test(expected = NullPointerException.class)
    public void testConvertClassPathToClassNamePathBasePathWithNullPath() throws Exception {
        ClassUtils.convertClassPathToClassName(null, BASE_PATH);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertClassPathToClassNamePathBasePathWithNullBasePath() throws Exception {
        ClassUtils.convertClassPathToClassName(PATH, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertClassPathToClassNamePathBasePathWithNullPathAndNullBasePath() throws Exception {
        ClassUtils.convertClassPathToClassName(null, null);
    }

}