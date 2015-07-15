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

import org.apache.commons.lang3.Validate;

import java.io.File;

/**
 * Utilities to handle Class names
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class ClassUtils {

    /**
     * Java package separator character
     */
    public static final String PACKAGE_SEPARATOR = ".";

    /**
     * Java class extension
     */
    public static final String CLASS_EXTENSION = ".class";

    /**
     * Convert given String path to class name according to the base one.
     * <p/>
     * Process is following:
     * 1. Remove base path from the to transform path
     * 2. Remove from it the first character if necessary
     * 3. Finally convert it to class name using the ClassUtils#convertClassPathToClassName(String)
     *
     * @param path     the String path to convert to class name
     * @param basePath the base path
     * @return the Class name translation of the given path based on the given base path
     */
    public static String convertClassPathToClassName(String path, String basePath) {
        Validate.notNull(path, "Unable to convert null path");
        Validate.notNull(basePath, "Unable to convert null base path");

        // Remove from path the base one
        String className = path.substring(basePath.length(), path.length());
        // Remove from it the first character if necessary
        className = className.startsWith(File.separator) ? className.substring(1) : className;
        // Then convert it to class name
        return ClassUtils.convertClassPathToClassName(className);
    }

    /**
     * Convert given String path to Class name
     * <p/>
     * Process is following:
     * 1. Remove the Class extension (.class)
     * 2. Replace any current File separator by the package ones (.)
     *
     * @param path the String path to convert to Class name
     * @return the Class name translation of the given String path
     */
    public static String convertClassPathToClassName(String path) {
        Validate.notNull(path, "Unable to convert null path");

        // Remove the Class extension
        String className = path.substring(0, path.lastIndexOf(CLASS_EXTENSION));
        // And replace any file separators by package ones
        return className.replaceAll(File.separator, PACKAGE_SEPARATOR);
    }
}
