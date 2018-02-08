[![Build Status][travis-badge]][travis-badge-url]

![](./img/aspectj-logo.jpg)

Spring Boot Source Weaving (Compile time) Example with AspectJ
===============================================================
This is an example of source weaving (compile time) with AspectJ.

### Source Weaving
AspectJ source weaving is compile-time weaving when all source code is available
including annotation class, aspect class, and target class.

The AspectJ compiler (`ajc`) processes the source code and generates woven
byte code. All the source code should be present together at the compile time.

### Dependency Requirements

#### AspectJ Runtime Library

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.13</version>
</dependency>
```

#### AspectJ Weaving Library
```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.8.13</version>
</dependency>
```

#### AspectJ Maven Plugin

```xml
<plugin>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>aspectj-maven-plugin</artifactId>
   <version>1.11</version>
   <configuration>
      <showWeaveInfo>true</showWeaveInfo>
      <source>1.8</source>
      <target>1.8</target>
      <complianceLevel>1.8</complianceLevel>
      <Xlint>ignore</Xlint>
      <forceAjcCompile>true</forceAjcCompile>
      <sources />
      <weaveDirectories>
         <weaveDirectory>${project.build.directory}/classes</weaveDirectory>
      </weaveDirectories>
   </configuration>
   <executions>
      <execution>
         <goals>
            <goal>compile</goal>
            <goal>test-compile</goal>
         </goals>
      </execution>
   </executions>
   <dependencies>
      <dependency>
         <groupId>org.aspectj</groupId>
         <artifactId>aspectjrt</artifactId>
         <version>1.8.13</version>
      </dependency>
      <dependency>
         <groupId>org.aspectj</groupId>
         <artifactId>aspectjtools</artifactId>
         <version>1.8.13</version>
      </dependency>
   </dependencies>
</plugin>
```

#### Lombok Maven Plugin (Optional)

```xml
<plugin>
   <groupId>org.projectlombok</groupId>
   <artifactId>lombok-maven-plugin</artifactId>
   <version>1.16.20.0</version>
   <executions>
      <execution>
         <phase>generate-sources</phase>
         <goals>
            <goal>delombok</goal>
         </goals>
      </execution>
   </executions>
   <configuration>
      <addOutputDirectory>false</addOutputDirectory>
      <sourceDirectory>src/main/java</sourceDirectory>
      <encoding>UTF-8</encoding>
   </configuration>
</plugin>
```

### Build
To build the JAR, execute the following command from the parent directory:

```
mvn clean install
```

### Run
Run the executable jar from the command to start the application,

```
java -jar spring-source-weaving-example-1.0.0.jar
```

### Usage
The application starts up at port `8080`. You can access the swagger UI at 
`http://localhost:8080/swagger-ui.html`. From the UI, you can create and retrieve
book entities.


[travis-badge]: https://travis-ci.org/indrabasak/spring-source-weaving-example.svg?branch=master
[travis-badge-url]: https://travis-ci.org/indrabasak/spring-source-weaving-example/