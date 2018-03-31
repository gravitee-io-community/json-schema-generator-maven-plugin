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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Aurelien Bourdon (aurelien.bourdon at gmail.com)
 */
public class ClassFinderTest {

    private static final Path ROOT_PATH = Paths.get(new File(ClassFinder.class.getResource("./samples").getPath()).getAbsolutePath());

    private static final Globs EMPTY_GLOBS = new Globs(null, null);
    private static final Globs JUST_INCLUDED_GLOBS = new Globs(Arrays.asList(new String[]{"**/One.class"}), null);
    private static final Globs JUST_EXCLUDED_GLOBS = new Globs(null, Arrays.asList(new String[]{"**/One.class"}));
    private static final Globs BOTH_INCLUDED_AND_EXCLUDED_GLOBS = new Globs(Arrays.asList(new String[]{"**"}), Arrays.asList(new String[]{"**/Two.class"}));

    @Test(expected = NullPointerException.class)
    public void testFindClassNamesWithNullRootPath() throws Exception {
        ClassFinder.findClassNames(null, EMPTY_GLOBS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassNamesWithNonExistingRootPath() throws Exception {
        ClassFinder.findClassNames(Paths.get("wrong"), EMPTY_GLOBS);
    }

    @Test(expected = NullPointerException.class)
    public void testFindClassNamesWithNullGlobs() throws Exception {
        ClassFinder.findClassNames(ROOT_PATH, null);
    }

    @Test
    public void testFindClassNamesWithEmptyGlobs() throws Exception {
        //List<String> expected = Arrays.asList(new String[]{"One", "Three", "Two"}); //we should use the full path for each of the classes
        List<String> expected = Arrays.asList(new String[]{oneClass, oneClass, threeClass, threeClass, twoClass, twoClass});
        List<String> actual = ClassFinder.findClassNames(ROOT_PATH, EMPTY_GLOBS);
        Collections.sort(actual);
        Assert.assertEquals(expected, actual);
    }
	
    public String oneClass=new String("./target/test-classes/io/gravitee/maven/plugins/json/schema/generator/util/samples/One.class");
    public String twoClass=new String("./target/test-classes/io/gravitee/maven/plugins/json/schema/generator/util/samples/Two.class");
    public String threeClass=new String("./target/test-classes/io/gravitee/maven/plugins/json/schema/generator/util/samples/Three.class");
	
    @Test
    public void testFindClassNamesWithJustIncludedGlobs() throws Exception {
       // List<String> expected = Arrays.asList(new String[]{"One"}); //we need to use the full classpath for samples/One.class instead
        List<String> expected = Arrays.asList(new String[]{oneClass,oneClass});
        List<String> actual = ClassFinder.findClassNames(Paths.get(new File(ClassFinder.class.getResource("./samples").getPath()).getAbsolutePath()),
        												new Globs(Arrays.asList(new String[]{"**/One.class"}), null));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindClassNamesWithJustExcludedGlobs() throws Exception {
        //List<String> expected = Arrays.asList(new String[]{"Three", "Two"}); //we shoud use fullpath for threeClass, twoClass
        List<String> expected = Arrays.asList(new String[]{threeClass, threeClass, twoClass, twoClass});
        List<String> actual = ClassFinder.findClassNames(ROOT_PATH, JUST_EXCLUDED_GLOBS);
        Collections.sort(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindClassNamesWithBothIncludedAndExcludedGlobs() throws Exception {
        //List<String> expected = Arrays.asList(new String[]{"One", "Three"}); //we should use fullpath for One.class and Three.class
        List<String> expected = Arrays.asList(new String[]{oneClass, oneClass, threeClass, threeClass});
        List<String> actual = ClassFinder.findClassNames(ROOT_PATH, BOTH_INCLUDED_AND_EXCLUDED_GLOBS);
        Collections.sort(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindClassNamesByUsingTheFormatGlobProcess() throws Exception {
        // must be turned into a valid file path before
        Path rootPath = Paths.get(new File(ClassFinder.class.getResource(".").getPath()).getAbsolutePath());
        //List<String> expected = Arrays.asList(new String[]{"samples.One"}); //we should use the full path for One.class instead
        List<String> expected = Arrays.asList(new String[]{oneClass, oneClass});
        List<String> actual = ClassFinder.findClassNames(rootPath, new Globs(Arrays.asList(new String[]{"One.class"}), null));
        Collections.sort(actual);
        Assert.assertEquals(expected, actual);
    }

}
