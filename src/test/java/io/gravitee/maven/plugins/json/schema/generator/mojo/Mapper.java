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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.customProperties.HyperSchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import io.gravitee.maven.plugins.json.schema.generator.util.ClassFinder;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Generates JSON Schemas from the matched Class Paths, by mapping Class attributes to the associated JSON field.
 * Mapper class is for TEST folder
 * @author Aurelien Bourdon (aurelien.bourdon at gmail.com)
 */
public class Mapper {

    /**
     * The associated Mojo configuration
     */
    private Config config;

    /**
     * Creates a new Mapper instance based on the given Mojo configuration
     *
     * @param config the Mojo configuration
     */
    public Mapper(Config config) {
        this.config = config;
    }

    /**
     * Generates JSON Schemas from the matched Class names
     *
     * @return a list of JSON Schemas from the matched Class names
     */
    public List<JsonSchema> generateJsonSchemas() 
    {
        final List<JsonSchema> generatedSchemas = new ArrayList<>();
		    System.out.println("Mapper::generateJsonSchemas (TEST) LINE 59 generatedSchemas="+generatedSchemas);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 61 mapper="+mapper);
        SchemaFactoryWrapper schemaVisitor = new HyperSchemaFactoryWrapper();
        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 63 schemaVisitor="+schemaVisitor);
        schemaVisitor.setVisitorContext(new LinkVisitorContext());
		    System.out.println("Mapper::generateJsonSchemas (TEST) LINE 65 generateClassNames()="+generateClassNames());
        for (String className : generateClassNames()) 
        {
            try 
            {
                try 
                {
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 68 mapper="+mapper);
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 69 className="+className);
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 70 schemaVisitor="+schemaVisitor);
					        String rootPath = Paths.get(config.getBuildDirectory()).toString();
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 73 rootPath="+rootPath);
					        String fullPath=rootPath+"\\io\\gravitee\\maven\\plugins\\json\\schema\\generator\\util\\samples\\One.class";
					        Class testClazz=null;
					        Class clazz=null;
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 75 fullPath="+fullPath);
					        clazz=this.getClass();
//unfortunately maven surefire has its own classloader so we need to relocate relevant classes (such as Mapper.java) INTO under this classloader
					        if(className.indexOf("Recursive")!=-1) clazz=io.gravitee.maven.plugins.json.schema.generator.mojo.samples.Recursive.class;
					        if(className.indexOf("One")!=-1)       clazz=io.gravitee.maven.plugins.json.schema.generator.util.samples.One.class;
					        if(className.indexOf("SimpleBean")!=-1) clazz=io.gravitee.maven.plugins.json.schema.generator.mojo.samples.SimpleBean.class;
					        if(className.indexOf("NoBean")!=-1)     clazz=io.gravitee.maven.plugins.json.schema.generator.mojo.samples.NoBean.class;
					        if(className.indexOf("BeanWithExternalDependency")!=-1) clazz=io.gravitee.maven.plugins.json.schema.generator.mojo.samples.BeanWithExternalDependency.class;
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 76 clazz="+clazz);
					        ClassLoader testClassLoader =null;
					        if(clazz!=null)           testClazz=clazz;
					        if(testClassLoader!=null) testClassLoader=clazz.getClassLoader();
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 77 testClassLoader="+testClassLoader);
					        try 
                  {
						        if(testClassLoader!=null) testClazz=clazz.forName(fullPath,true,testClassLoader);
						        else                      testClazz=clazz.forName(fullPath);
					        } 
                  catch(Exception cnfe) { ; }
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 77 testClazz="+testClazz);
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 80 testClassLoader="+testClassLoader);
                  mapper.acceptJsonFormatVisitor(mapper.constructType(testClazz
						    //testClassLoader.loadClass(className)
                //getClass().getClassLoader().loadClass(className)
                    ), schemaVisitor);
                  System.out.println("Mapper::generateJsonSchemas (TEST) LINE 85 mapper="+mapper);
                } 
                catch (JsonMappingException e) 
                {
					        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 75 throws JsonMappingException message="+e.getMessage());
                  throw new GenerationException("Mapper::generateJsonSchemas (TEST) LINE 76 Unable to format class=" + className, e);
                }
                System.out.println("Mapper::generateJsonSchemas (TEST) LINE 73 schemaVisitor="+schemaVisitor);
                JsonSchema schema = schemaVisitor.finalSchema();
                System.out.println("Mapper::generateJsonSchemas (TEST) LINE 74 schema="+schema);
                if (schema == null) 
                {
                    throw new IllegalArgumentException("MapperSchema::generateJsonSchemas (TEST) LINE 83 Could not build schema or find any classes.");
                }
                System.out.println("Mapper::generateJsonSchemas (TEST) LINE 85 schema="+schema);
                generatedSchemas.add(schema);
            }
          //catch(ClassNotFoundException  cnfe)
          //{   //surefire uses its own classloader and will fail any class located in test-classes folder
			      //	System.out.println("Mapper::generateJsonSchemas (TEST) LINE 90 throws ClassNotFoundException message="+cnfe.getMessage());
			    //}
            catch (GenerationException e) {
                System.out.println("Mapper::generateJsonSchemas (TEST) LINE 93 Unable to generate JSON schema for class " + className+" message="+e.getMessage());
            }
        }
        System.out.println("Mapper::generateJsonSchemas (TEST) LINE 96 returns generatedSchemas="+generatedSchemas);
        return generatedSchemas;
    }

    /**
     * Generates Class names from the matched Paths
     *
     * @return a list of Class names from the matched Paths
     */
    private List<String> generateClassNames() 
    {
        Set<String> classNames = new HashSet<>();
        Path root = Paths.get(config.getBuildDirectory());
        if (!root.toFile().isDirectory()) 
        {
            config.getLogger().error("Invalid underlying project build directory: " + config.getBuildDirectory());
            return Collections.<String>emptyList();
        }
        try 
        {
            classNames.addAll(ClassFinder.findClassNames(root, config.getGlobs()));
            config.getLogger().debug("Generated class names: " + classNames);
        } 
        catch (IOException e) 
        {
            config.getLogger().warn("Unable to generate JSON schemas", e);
        }
        return new ArrayList<>(classNames);
    }

    private static class GenerationException extends Exception 
    {
        public GenerationException(String message, Throwable cause) 
        {
            super(message, cause);
        }
    }
}
