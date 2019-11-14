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
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class to find Classes from a given Path
 *
 * @author Aurélien Bourdon (aurelien.bourdon at gmail.com)
 */
public class ClassFinder {

    /**
     * Java class extension
     */
    private static final String CLASS_EXTENSION = ".class";

    /**
     * Find class names from the given root Path that matching the given list of globs.
     * <p/>
     * Class names are built following the given rule:
     * - Given the root Path: /root/path/
     * - Given the Class Path: /root/path/the/path/to/the/class/Class.class
     * - Then associated Class name would be: the.path.to.the.class.Class
     *
     * @param root  the root Path from which start searching
     * @param globs the glob list to taking into account during path matching
     * @return a list of Paths that match the given list of globs from the root Path
     * @throws IOException if an I/O occurs
     */
    public static List<String> findClassNames(Path root, Globs globs) throws IOException {
        Validate.notNull(root, "Unable to handle null root path");
        Validate.isTrue(root.toFile().isDirectory(), "Unable to handle non existing or non directory root path");
        Validate.notNull(globs, "Unable to handle null globs");

        List<String> matchedClassNames = new ArrayList<>();
        matchedClassNames.addAll(findClassPaths(root, globs)
                .stream()
                .map(path -> ClassUtils.convertClassPathToClassName(path.toString(), root.normalize().toString()))
                .collect(Collectors.toList()));
        try {
            if(Objects.nonNull(globs.getScanPackage()) && Objects.nonNull(globs.getParentClass())) {
                Reflections reflections = new Reflections(globs.getScanPackage());
                for(Object subType : reflections.getSubTypesOf(Class.forName(globs.getParentClass()))) {
                    Class subTypeClass = (Class) subType;
                    if(!Modifier.isAbstract(subTypeClass.getModifiers()) && !Modifier.isInterface(subTypeClass.getModifiers())) {
                        matchedClassNames.add(subTypeClass.getName());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return matchedClassNames;
    }

    /**
     * Find class Paths from the given root Path that match the given list of globs.
     *
     * @param root  the root Path from which start searching
     * @param globs the glob list to taking into account during path matching
     * @return a list of Paths that match the given list of globs from the root Path
     * @throws IOException if an I/O occurs
     */
    private static List<Path> findClassPaths(Path root, Globs globs) throws IOException {
        List<Path> matchedPaths = new ArrayList<>();
        Files.walkFileTree(root.normalize(), new GlobsMatchingClassFileVisitor(globs, matchedPaths));
        return matchedPaths;
    }

    /**
     * Visit a given root Path by getting class Paths that match the list of given glob patterns
     */
    private static class GlobsMatchingClassFileVisitor extends SimpleFileVisitor<Path> {

        /**
         * The associated GlobPathMatchers
         */
        private final GlobPathMatchers globPathMatchers;

        /**
         * Path list result
         */
        private final List<Path> matchedPaths;

        /**
         * Create a new instance based on the given included/excluded globs. Results will be stored into the given mactchedPaths
         *
         * @param globs        wrapper to the included/excluded globs configuration
         * @param matchedPaths the structure to store results
         */
        public GlobsMatchingClassFileVisitor(Globs globs, List<Path> matchedPaths) {
            this.globPathMatchers = new GlobPathMatchers(globs);
            this.matchedPaths = matchedPaths;
        }

        /**
         * Simplify glob writing by automatically add the double wildcard pattern
         *
         * @param glob the glob to format
         * @return a new formatted glob
         */
        private static String formatGlob(String glob) {
            glob = "**/" + glob;
            return glob;
        }

        /**
         * Visit the given file and test it against the current globPathMatchers
         *
         * @param path           the path to test against the current globPathMatchers
         * @param fileAttributes the associated file attributes
         * @return always FileVisitResult.CONTINUE
         */
        public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
            // If Path is not a class file then continue
            if (!path.getFileName().toString().endsWith(CLASS_EXTENSION)) {
                return FileVisitResult.CONTINUE;
            }

            // Do not keep classes matching the excluded globs
            for (PathMatcher excludedMatcher : globPathMatchers.getExcludedMatchers()) {
                if (excludedMatcher.matches(path)) {
                    return FileVisitResult.CONTINUE;
                }
            }

            if (globPathMatchers.getIncludedMatchers().isEmpty()) {
                return FileVisitResult.CONTINUE;
            }

            // Else only keep Path matching the included paths
            for (PathMatcher includedMatcher : globPathMatchers.getIncludedMatchers()) {
                if (includedMatcher.matches(path)) {
                    matchedPaths.add(path);
                    return FileVisitResult.CONTINUE;
                }
            }

            return FileVisitResult.CONTINUE;
        }

        /*
         * Wrapper to included/excluded PathMachers
         */
        private static class GlobPathMatchers {
            /**
             * PathMatcher list of included paths
             */
            private final List<PathMatcher> includedMatchers;

            /**
             * PathMatcher list of excluded paths
             */
            private final List<PathMatcher> excludedMatchers;

            /**
             * Create a new instance based on the given Globs
             *
             * @param globs the based Globs to create included/excluded PathMatchers
             */
            public GlobPathMatchers(Globs globs) {
                this.includedMatchers = new ArrayList<>();
                this.excludedMatchers = new ArrayList<>();
                if (!Objects.nonNull(globs.getScanPackage()) && !Objects.nonNull(globs.getParentClass())) {
                    includedMatchers.addAll(globs.getIncludes()
                            .stream()
                            .map(include -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(include)))
                            .collect(Collectors.toList())
                    );
                    excludedMatchers.addAll(globs.getExcludes()
                        .stream()
                        .map(exclude -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(exclude)))
                        .collect(Collectors.toList())
                    );
                }
            }

            /**
             * @return the list of included PathMatchers
             */
            public List<PathMatcher> getIncludedMatchers() {
                return includedMatchers;
            }

            /**
             * @return the list of excluded PathMatchers
             */
            public List<PathMatcher> getExcludedMatchers() {
                return excludedMatchers;
            }

        }

    }

}
