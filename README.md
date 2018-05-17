[![Build Status][travis-badge]][travis-badge-url]
[![Quality Gate][sonarqube-badge]][sonarqube-badge-url] 
[![Technical debt ratio][technical-debt-ratio-badge]][technical-debt-ratio-badge-url] 
[![Coverage][coverage-badge]][coverage-badge-url]

![](./img/aspectj-source-weaving-logo.svg)

Spring Boot Source Weaving (Compile time) Example with AspectJ
===============================================================
This is an example of Spring Boot source weaving (compile time) with AspectJ.

### Source Weaving
AspectJ source weaving is compile-time weaving when all source code is available
including annotation class, aspect class, and target class.

The AspectJ compiler (`ajc`) processes the source code and generates woven
byte code. All the source code should be present together at the compile time.

![](./img/aspectj-source-weaving.svg)

### When do you need source weaving?
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

### Project Description
1. A `CustomAnnotation` annotation to intercept any method.

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {

    String description() default "";
}
```

1. A `CustomAnnotationAspect` aspect to intercept any method marked with
`@CustomAnnotation`. It prints out the name of the intercepted class and method.

```java
@Component
@Aspect
@Slf4j
public class CustomAnnotationAspect {

    @Before("@annotation(anno) && execution(* *(..))")
    public void inspectMethod(JoinPoint jp, CustomAnnotation anno) {
        log.info(
                "Entering CustomAnnotationAspect.inspectMethod() in class "
                        + jp.getSignature().getDeclaringTypeName()
                        + " - method: " + jp.getSignature().getName()
                        + " description: " + anno.description());
    }
}
```

1. The `BookService` class is the example where the `@CustomAnnotation` is used.
The **privat**e method `validateRequest` is called from `create` method. The
`create` method is annotated with Spring's `@Transactional` annotation.

```java
@Service
@Slf4j
public class BookService {

    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Book create(BookRequest request) {
        Book entity = validateRequest(request);
        return repository.save(entity);
    }

    public Book read(UUID id) {
        return repository.getOne(id);
    }

    @CustomAnnotation(description = "Validates book request.")
    private Book validateRequest(BookRequest request) {
        log.info("Validating book request!");

        Assert.notNull(request, "Book request cannot be empty!");
        Assert.notNull(request.getTitle(), "Book title cannot be missing!");
        Assert.notNull(request.getAuthor(), "Book author cannot be missing!");

        Book entity = new Book();
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());

        return entity;
    }
}
```

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
Once the application starts up at port `8080`, you can access the swagger UI at 
`http://localhost:8080/swagger-ui.html`. From the UI, you can create and retrieve
book entities.

Once you create a book entity, you should notice the following message on the
terminal:

```
2018-02-08 09:46:55.429  INFO 29924 --- [nio-8080-exec-1] c.basaki.aspect.CustomAnnotationAspect   : Entering CustomAnnotationAspect.inspectMethod() in class com.basaki.service.BookService - method: validateRequest description: Validates book request.
2018-02-08 09:46:55.429  INFO 29924 --- [nio-8080-exec-1] com.basaki.service.BookService           : Validating book request!
```


[travis-badge]: https://travis-ci.org/indrabasak/spring-source-weaving-example.svg?branch=master
[travis-badge-url]: https://travis-ci.org/indrabasak/spring-source-weaving-example/

[sonarqube-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-source-weaving-example&metric=alert_status
[sonarqube-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-source-weaving-example 

[technical-debt-ratio-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-source-weaving-example&metric=sqale_index
[technical-debt-ratio-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-source-weaving-example 

[coverage-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-source-weaving-example&metric=coverage
[coverage-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-source-weaving-example
