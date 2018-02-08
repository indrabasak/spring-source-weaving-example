[![Build Status][travis-badge]][travis-badge-url]

![](./img/aspectj-logo.jpg)

Spring Boot Source Weaving (Compile time) Example with AspectJ
===============================================================
This is an example of Spring Boot source weaving (compile time) with AspectJ.

### Source Weaving
AspectJ source weaving is compile-time weaving when all source code is available
including annotation class, aspect class, and target class.

The AspectJ compiler (`ajc`) processes the source code and generates woven
byte code. All the source code should be present together at the compile time.

### Why do you need source weaving?
1. Due to the proxy-based nature of Spring’s AOP framework, calls within the 
target object are by definition not intercepted.

1. For JDK proxies, only public interface method calls on the proxy can be 
intercepted. With CGLIB, public and protected method calls on the proxy will 
be intercepted, and even package-visible methods if necessary.

You can find more [here](https://docs.spring.io/spring/docs/4.3.x/spring-framework-reference/html/aop.html#Supported%20Pointcut%20Designators).

In other words, 

1. Any call to a **private method** will not be intercepted. Please refer to 
the second point mentioned above.
                
1. Any call to method **methodB** of class **ClassX*** from **methodA** of 
class **ClassX** will not be intercepted since they belong to the same target 
object. Please refer to the first point above.

AspectJ source weaving will help you get past the above limitations posed by
Spring AOP.

### Dependency Requirements

#### AspectJ Runtime Library
Annotation such as `@Aspect`, `@Pointcut`, and `@Before` are in `aspectjrt.jar`.
The `aspectjrt.jar` and must be in the classpath regardless of whether 
the aspects in the code are compiled with `ajc` or `javac`.

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.13</version>
</dependency>
```

#### AspectJ Weaving Library
The `aspectjweaver.jar` contains the AspectJ wevaing classes. The weaver is 
responsible for mapping crosscutting elements to Java constructs.

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.8.13</version>
</dependency>
```

#### AspectJ Maven Plugin
The `aspectj-maven-plugin` plugin is used for weaving AspectJ aspects into 
the classes using `ajc` (AspectJ compiler) during compile time.

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
The `lombok-maven-plugin` is required only if any of the classes uses Lombok
annotations. The Lombok annotated classes are delomboked before compiled with
the `ajc`.

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