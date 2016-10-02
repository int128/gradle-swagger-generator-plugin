Gradle Swagger Codegen Plugin [![Build Status](https://travis-ci.org/int128/gradle-swagger-codegen-plugin.svg?branch=master)](https://travis-ci.org/int128/gradle-swagger-codegen-plugin)
=============================

A Gradle plugin to generate API server or client code by [Swagger Codegen](https://github.com/swagger-api/swagger-codegen).


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
  // declare swaggerCodegen to run CLI
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.2.1'
}

// declare a task to generate code
task generateServer(type: SwaggerCodegen) {
  language = 'spring'
  inputFile = file('petstore.yaml')
  outputDir = file("$buildDir/generated/server")
}
```

The plugin adds a `swaggerCodegenHelp` task to list available languages and JSON configuration of declared tasks.

```
% ./gradlew swaggerCodegenHelp
:swaggerCodegenHelp
Available languages: [android...]
Available JSON configuration for task ':generateServer':

CONFIG OPTIONS
	sortParamsByRequiredFlag
  ...
```


Using Custom Generator
----------------------

We can use a custom template and custom generator class.

Task type `SwaggerCodegen` accepts below properties.

Key           | Value                                             | Example value
--------------|---------------------------------------------------|--------------
`language`    | Language to generate (required)                   | `spring`
`library`     | Library (optional)                                | `spring-mvc`
`inputFile`   | Swagger spec file (required)                      | `file('swagger.yaml')`
`outputDir`   | Directory to write the generated files (required) | `file("$buildDir/generated")`
`configFile`  | JSON configuration file (optional)                | `file('swagger.config.json')`
`templateDir` | Directory containing the template (optional)      | `file('src/template')`

Since task type `SwaggerCodegen` is a [`JavaExec` task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html),
it accepts `JavaExec` properties such as `classpath` or `systemProperties`.

See [custom-generator project in acceptance-test](acceptance-test/custom-generator) for more.


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
