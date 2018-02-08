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