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

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** * Utility class to find Classes from a given Path * * @author AurÃ©lien Bourdon (aurelien.bourdon at gmail.com) */
public class ClassFinder {

    /** * Java class extension */
    private static final String CLASS_EXTENSION = ".class";

    /** * Find class names from the given root Path that matching the given list of globs. * <p/> * Class names are built following the given rule:
	 * - Given the root Path: /root/path/ * - Given the Class Path: /root/path/the/path/to/the/class/Class.class * - Then associated Class name
	 would be: the.path.to.the.class.Class * * @param root  the root Path from which start searching * @param globs the glob list to taking into
	 account during path matching * @return a list of Paths that match the given list of globs from the root Path * @throws IOException if an I/O
	 occurs */
	 public static java.util.stream.Stream<java.lang.String> outputStream=null; public static List<String> findClassNames(Path root,Globs globs) throws IOException
	 {
		 Validate.notNull(root, "Unable to handle null root path");
		 Validate.isTrue(root.toFile().isDirectory(),"Unable to handle non existing or non directory root path");
		 Validate.notNull(globs, "Unable to handle null globs");

        List<String> matchedClassNames = new ArrayList<>(); 
        System.out.println("ClassFinder::findClassNames LINE 58 root="+root);
        System.out.println("ClassFinder::findClassNames LINE 59 globs="+globs);
        System.out.println("ClassFinder::findClassNames LINE 60 matchedClassNames="+matchedClassNames);
        //System.out.println("ClassFinder::findClassNames LINE 61 matchedClassNames.addAll(findClassPaths(root,globs)="+matchedClassNames.addAll(findClassPaths(root,globs)));
	//System.out.println("ClassFinder::findClassNames LINE 62 matchedClassNames.addAll(findClassPaths(root,globs).stream()="+matchedClassNames.addAll(findClassPaths(root,globs).stream()));
	//System.out.println("ClassFinder::findClassNames LINE 63 matchedClassNames.addAll(findClassPaths(root,globs).collect(Collectors.toList())="+matchedClassNames.addAll(findClassPaths(root,globs).collect(Collectors.toList())));
	//System.out.println("ClassFinder::findClassNames LINE 63 matchedClassNames.addAll(findClassPaths(root,globs).map(path -> ClassUtils.convertClassPathToClassName(path.toString(),root.normalize().toString()))="+ java.util.Collection<java.nio.file.Path> collection=findClassPaths(root, globs));
	System.out.println("ClassFinder::findClassNames LINE 64 collection="+collection);
	for(int i=0;i<collection.size();i++)
	{
		matchedClassNames.add(collection.toArray()[i].toString());
	}
	//        
		 boolean b=((List<String>)matchedClassNames).addAll(collectionStrings);
		System.out.println("ClassFinder::findClassNames LINE 61 outputStream="+outputStream);
		if(outputStream!=null)
		{
			outputStream.close();
			outputStream = null;
		}
		if(outputStream==null) outputStream=matchedClassNames.stream();
		System.out.println("ClassFinder::findClassNames LINE 63 outputStream="+outputStream);
		//if(outputStream!=null) outputStream.map(path -> ClassUtils.convertClassPathToClassName(path.toString(), root.normalize().toString()));
		//System.out.println("ClassFinder::findClassNames LINE 65 outputStream="+outputStream);
		try {
			//System.out.println("ClassFinder::findClassNames LINE 66 outputStream.map(path -> ClassUtils.convertClassPathToClassName(path.toString(),root.normalize().toString()))="+outputStream.map(path -> ClassUtils.convertClassPathToClassName(path.toString(),
			root.normalize().toString()))); matchedClassNames.addAll(outputStream //findClassPaths(root, globs) //.stream() .map(path ->
			ClassUtils.convertClassPathToClassName(path.toString(), root.normalize().toString())) .collect(Collectors.toList()));
			//for some  reason we get IllegalStateException with stream already closed
		}
		catch(IllegalStateException illegal)
		{ //try closing
			System.out.println("!!! IllegalStateException LINE 88 has been thrown attempting outputStream.close()");
			outputStream.close();
			//try to reopen System.out.println("!!! IllegalStateException LINE 91 has been thrown attempting re-initialization of stream");
			outputStream = matchedClassNames.stream();
			System.out.println("!!! IllegalStateException LINE 93 has been thrown (re-initialized) outputStream="+outputStream);
			System.out.println("!!! IllegalStateException LINE 94 has been thrown message ="+illegal);
		}
		System.out.println("ClassFinder::findClassNames LINE 70 returns matchedClassNames="+matchedClassNames);
		return matchedClassNames;
		}

    /** * Find class Paths from the given root Path that match the given list of globs.
      * * @param root  the root Path from which start searching
	  * @param globs the glob list to taking into account during path matching
	  * @return a list of Paths that match the given list of globs from the root Path
	  * @throws IOException if an I/O occurs */
	 private static List<Path> findClassPaths(Path root, Globs globs) throws IOException
	 {
		 List<Path> matchedPaths = new ArrayList<>();
		 System.out.println("ClassFinder::findClassPaths LINE 75 root="+root);
		 System.out.println("ClassFinder::findClassPaths LINE 75 root.normalize()="+root.normalize());
		 System.out.println("ClassFinder::findClassPaths LINE 76 globs="+globs);
		 System.out.println("ClassFinder::findClassPaths LINE 77 new GlobsMatchingClassFileVisitor(globs, matchedPaths)="+new GlobsMatchingClassFileVisitor(globs, matchedPaths)); Files.walkFileTree(root.normalize(), new GlobsMatchingClassFileVisitor(globs,matchedPaths));
		 System.out.println("ClassFinder::findClassPaths LINE 80 matchedPaths="+matchedPaths);
		 return matchedPaths;
	}

    /** * Visit a given root Path by getting class Paths that match the list of given glob patterns */
	private static class GlobsMatchingClassFileVisitor extends SimpleFileVisitor<Path>
	 {
        /** * The associated GlobPathMatchers */
        private final GlobPathMatchers globPathMatchers;

        /** * Path list result */
        private final List<Path> matchedPaths;

        /** * Create a new instance based on the given included/excluded globs. Results will be stored into the given mactchedPaths
          * * @param globs        wrapper to the included/excluded globs configuration
          * @param matchedPaths the structure to store results */
        public GlobsMatchingClassFileVisitor(Globs globs, List<Path> matchedPaths)
        {
			System.out.println("ClassFinder::GlobsMatchingClassFileVisitor(globs="+globs);
			this.globPathMatchers = new GlobPathMatchers(globs);
			System.out.println("ClassFinder::GlobsMatchingClassFileVisitor( globPathMatchers="+globPathMatchers);
			this.matchedPaths = matchedPaths;
	}

        /** * Simplify glob writing by automatically add the double wildcard pattern
          * * @param glob the glob to format * @return a new formatted glob */
        private static String formatGlob(String glob) { glob = "**/" + glob; return glob; }

        /** * Visit the given file and test it against the current globPathMatchers
          * * @param path           the path to test against the current globPathMatchers
          * @param fileAttributes the associated file attributes
          * @return always FileVisitResult.CONTINUE */
        public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes)
        { // If Path is not a class file then continue
            if (!path.getFileName().toString().endsWith(CLASS_EXTENSION)) { return FileVisitResult.CONTINUE; }

            // Do not keep classes matching the excluded globs
            for (PathMatcher excludedMatcher : globPathMatchers.getExcludedMatchers())
            {
				if(excludedMatcher.matches(path)) { return FileVisitResult.CONTINUE; }
	    }

            // If there is no included paths, then keep them all
            if (globPathMatchers.getIncludedMatchers().isEmpty())
            {
		matchedPaths.add(path);
		return FileVisitResult.CONTINUE;
	    }

            // Else only keep Path matching the included paths
            for (PathMatcher includedMatcher : globPathMatchers.getIncludedMatchers())
            {
		if (includedMatcher.matches(path)) { matchedPaths.add(path); return FileVisitResult.CONTINUE; }
	    }
            return FileVisitResult.CONTINUE;
         }

        /** * Wrapper to included/excluded PathMachers */
         private static class GlobPathMatchers
         { /** * PathMatcher list of included paths */
	    private final List<PathMatcher> includedMatchers;

            /** * PathMatcher list of excluded paths */
            private final List<PathMatcher> excludedMatchers;

            /** * Create a new instance based on the given Globs * * @param globs the based Globs to create included/excluded PathMatchers */
	    public GlobPathMatchers(Globs globs)
	    {
		this.includedMatchers = new ArrayList<>();
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 165 globs="+globs);
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 166 globs.getIncludes()="+globs.getIncludes());
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 167 globs.getIncludes().stream()="+globs.getIncludes().stream().map(include -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(include))));
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 168 globs.getIncludes().stream().collect(Collectors.toList())="+globs.getIncludes().stream());
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 169 globs.getIncludes().stream().collect(Collectors.toList())="+globs.getIncludes().stream().collect(Collectors.toList()));
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 170 globs.getIncludes().map(include -> FileSystems.getDefault().getPathMatcher('glob:' + formatGlob(include))"+globs.getIncludes().stream().map(include -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(include))));
		includedMatchers.addAll(globs.getIncludes() .stream() .map(include -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(include))).collect(Collectors.toList()) );
		System.out.println("ClassFinder::GlobPathMatchers(Globs) LINE 176 includedMatchers="+includedMatchers);
		this.excludedMatchers = new ArrayList<>();
		System.out.println("ClassFinder::GlobPathMatchers LINE 178 globs.getExcludes()="+globs.getExcludes());
		//System.out.println("ClassFinder::GlobPathMatchers LINE 179 globs.getExcludes().stream()="+stream());
		System.out.println("ClassFinder::GlobPathMatchers LINE 180 globs.getExcludes().stream().collect(Collectors.toList())="+globs.getExcludes().stream().collect(Collectors.toList()));
		//System.out.println("ClassFinder::GlobPathMatchers LINE 181 exclude="+exclude -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(exclude)));
		//System.out.println("ClassFinder::GlobPathMatchers LINE 182 formatGlob(exclude)=" + exclude -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(exclude)) );
		System.out.println("ClassFinder::GlobPathMatchers LINE 183 globs.getExcludes().stream().map(exclude -> FileSystems.getDefault().getPathMatcher('glob:' + formatGlob(exclude)))="+globs.getExcludes().stream().map(exclude -> FileSystems.getDefault().getPathMatcher("glob:" +formatGlob(exclude))));
		excludedMatchers.addAll(globs.getExcludes() .stream() .map(exclude -> FileSystems.getDefault().getPathMatcher("glob:" + formatGlob(exclude))) .collect(Collectors.toList()) );
		System.out.println("ClassFinder::GlobPathMatchers LINE 189 excludedMatchers="+excludedMatchers);
	    }

            /** * @return the list of included PathMatchers */
            public List<PathMatcher> getIncludedMatchers() { return includedMatchers; }

            /** * @return the list of excluded PathMatchers */
            public List<PathMatcher> getExcludedMatchers() { return excludedMatchers; }
         }
    }
}
