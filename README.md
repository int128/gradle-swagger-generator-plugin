Gradle Swagger Codegen Plugin
=============================

A Gradle plugin to generate code by [Swagger Codegen](https://github.com/swagger-api/swagger-codegen).


Getting Started
---------------

Create following build script and run `generateServer` task to generate code.

```groovy
plugins {
  id 'org.hidetake.swagger.codegen' version '1.0.0'
}

repositories {
  jcenter()
}

dependencies {
  // declare a dependency as swaggerCodegen
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.2.1'
}

// declare a task to generate code
task generateServer(type: SwaggerCodegen) {
  // set options
  language = 'spring'
  inputFile = file('petstore.yaml')
  outputDir = file("$buildDir/generated/server")
}
```


Tasks
-----

Task type `SwaggerCodegen` accepts below options.

Key           | Value                                             | Example
--------------|---------------------------------------------------|--------
`language`    | Language to generate (required)                   | `spring`
`library`     | Library (optional)                                | `spring-mvc`
`inputFile`   | Swagger spec file (required)                      | `file(...)`
`outputDir`   | Directory to write the generated files (required) | `file(...)`
`configFile`  | JSON configuration file (optional)                | `file(...)`
`templateDir` | Directory containing the template (optional)      | `file(...)`

Task type `SwaggerCodegen` is a [`JavaExec` task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html).
`JavaExec` properties such as `classpath` or `systemProperties` can be set in a task.

Run `swaggerCodegenHelp` task to list available languages and JSON configuration of each tasks.

```
% ./gradlew swaggerCodegenHelp
:swaggerCodegenHelp
Available languages: [android...]
Available JSON configuration for task ':generateServer':

CONFIG OPTIONS
	sortParamsByRequiredFlag
  ...
```


Contributions
-------------

This is an open source software licensed under the Apache License Version 2.0.
Feel free to open issues or pull requests.


### Working with Travis CI

Travis CI builds the plugin continuously.
It also publishes the plugin if a tag is pushed and following variables are set.

Environment Variable        | Value
----------------------------|------
`$GRADLE_PUBLISH_KEY`       | `gradle.publish.key` of the API key
`$GRADLE_PUBLISH_SECRET`    | `gradle.publish.secret` of the API key
