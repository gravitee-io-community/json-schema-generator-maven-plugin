# JSON Schema Generator Maven plugin

Maven plugin to generate JSON Schemas.

Using the [Jackson JSON Schema Module](https://github.com/FasterXML/jackson-module-jsonSchema) for JSON Schema generation.

[![Build Status](http://build.gravitee.io/jenkins/buildStatus/icon?job=json-schema-generator-maven-plugin)](http://build.gravitee.io/jenkins/job/json-schema-generator-maven-plugin/)

## Usage

```xml
<build>
 <plugins>
  ...
  <plugin>
   <groupId>io.gravitee.maven.plugins</groupId>
   <artifactId>json-schema-generator-maven-plugin</artifactId>
   <version>1.0-SNAPSHOT</version>
   <executions>
    <execution>
     <phase>prepare-package</phase>
     <goals>
      <goal>generate-json-schemas</goal>
     </goals>
     <configuration>
      <includes>
	   <include>path/to/included/classes/*.class</include>
      </includes>
      <excludes>
       <exclude>path/to/excluded/classes/ToExclude.class</exclude>
      </excludes>
      <buildDirectory>${project.build.outputDirectory}</buildDirectory>
      <outputDirectory>${project.build.outputDirectory}/schemas</outputDirectory>
     </configuration>
    </execution>
   </executions>
  </plugin>
  ...
 </plugins>
</build>
```

## Configuration

### Goals

#### generate-json-schemas

##### Definition
Generate JSON Schemas satisfying configuration part. JSON Schemas are generated from **.class** files that have to represent a [Java Bean](https://en.wikipedia.org/wiki/JavaBeans) class. Classes are found by searching from the underlying project's classes build directory, *i.e.,* from the ``${project.build.outputDirectory}``.

Note that external dependencies (Maven dependencies) are also used but only during JSON Schema **properties** resolution.

##### Configuration

###### includes *(optional)*
List of .class file paths to include.

Can use [glob](https://en.wikipedia.org/wiki/Glob_(programming)) syntax.
If absent, then all .class files are included.
Note that the ``**/`` glob pattern is automatically added as a prefix to lighten the writing. So there is no need to specificy absolute paths. Just take care about class name, but by using path separators instead of dot ones.

*Examples:*

Generate the `package.to.class.Class` would be written as:
```xml
 <include>package/to/class/Class.class</include>
```

Generate all classes from the package ``package.to.class`` would be written as:
```xml
<include>package/to/class/**</include>
```

Generate all classes ending by ``Schema`` would be written as:
```xml
<include>*Schema.class</include>
```

###### excludes *(optional)*
List of .class file paths to exclude.

Same syntax as the includes part (see above). If absent, then no exclusion is done.
Note that excludes are computed **before** includes.

*Examples:*

Exclude any class that ends by ``Utils``:
```xml
<exclude>*Utils.class</exclude>
```

Remove inclusion from the ``path/to/class`` package:
```xml
<includes>
 <include>path/to/class/Class.class</include>
</includes>
<excludes>
 <exclude>path/to/class/**</exclude>
</excludes>
```

###### buildDirectory *(optional)*
Path to the .class files from which find and generate JSON Schemas.

Default value: ``${project.build.outputDirectory}``

###### outputDirectory *(optional)*
Output directory to put generated JSON Schemas.

Default value: ``${project.build.outputDirectory}/schemas``
