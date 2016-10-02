# Example project using custom generator

This folder contains the example of custom generator.

This contains following features and each changes.

- Use custom generator written in Groovy
  - `build.gradle`
  - `src/main/groovy/MySpringCodegen.groovy`
  - Extending [`SpringCodegen` class](https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-codegen/src/main/java/io/swagger/codegen/languages/SpringCodegen.java)
- Use custom template
  - `build.gradle`
  - `src/template/server`
  - Modifying [JavaSpring template](https://github.com/swagger-api/swagger-codegen/tree/master/modules/swagger-codegen/src/main/resources/JavaSpring)
- Change package name and library
  - `server.swagger.json`
