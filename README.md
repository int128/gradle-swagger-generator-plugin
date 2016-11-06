Gradle Swagger Generator Plugin [![Build Status](https://travis-ci.org/int128/gradle-swagger-generator-plugin.svg?branch=master)](https://travis-ci.org/int128/gradle-swagger-generator-plugin)
=============================

A Gradle plugin to generate API server or client code by [Swagger Codegen](https://github.com/swagger-api/swagger-codegen).


Getting Started
---------------

Create following build script and run `generateServer` task to generate code.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '1.1.0'
}

repositories {
  jcenter()
}

dependencies {
  // define swaggerCodegen to run Swagger Codegen CLI
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.2.1'
}

// define a task to generate code
task generateServer(type: SwaggerCodegen) {
  language = 'spring'
  inputFile = file('petstore.yaml')
  outputDir = file("$buildDir/generated/server")
}
```


Customize code generation
-------------------------

The plugin supports a custom template and custom generator class.

Task type `SwaggerCodegen` accepts below properties.

Key           | Type              | Value                                   | Example value
--------------|-------------------|-----------------------------------------|--------------
`language`    | String, required  | Language to generate                    | `spring`
`library`     | String, optional  | Library                                 | `spring-mvc`
`inputFile`   | File, required    | Swagger spec file                       | [`file('petstore.yaml')`](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/yaml/petstore.yaml)
`outputDir`   | File, required    | Directory to write the generated files  | `file("$buildDir/generated")`
`configFile`  | File, optional    | [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator) | `file('swagger.config.json')`
`templateDir` | File, optional    | Directory containing the template       | `file('src/template')`
`components`  | List of Strings   | [Components to generate](https://github.com/swagger-api/swagger-codegen#selective-generation) that is a list of `models`, `apis` and `supportingFiles`. Defaults to all components | `['models', 'apis']`

Since task type `SwaggerCodegen` is a [`JavaExec` task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html),
it accepts `JavaExec` properties such as `classpath` or `systemProperties`.

See also [simple-generation project](acceptance-test/simple-generation) and [custom-generator project](acceptance-test/custom-generator) for more.

Run `swaggerCodegenHelp` task to list available languages and JSON configuration of defined tasks.

```
% ./gradlew swaggerCodegenHelp
:swaggerCodegenHelp
Available languages: [android, ...]
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
