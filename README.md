Gradle Swagger Generator Plugin [![Build Status](https://travis-ci.org/int128/gradle-swagger-generator-plugin.svg?branch=master)](https://travis-ci.org/int128/gradle-swagger-generator-plugin)
=============================

This is a Gradle plugin to generate server code, client code and API document.
It depends on [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) and [Swagger2Markup](https://github.com/Swagger2Markup/swagger2markup).


Getting Started: Code Generation
--------------------------------

Create following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '1.2.1'
}

repositories {
  jcenter()
}

dependencies {
  // define dependency for Swagger Codegen CLI
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.2.1'
}

generateSwaggerCode {
  language = 'spring'
  inputFile = file('petstore.yaml')
  configFile = file('config.json')
  components = ['models', 'apis']
}
```

Run the task to generate code.

```
% ./gradlew generateSwaggerCode
:generateSwaggerCode
[main] INFO io.swagger.parser.Swagger20Parser - reading from ...
```

Run the task with `Help` postfix to show available JSON configuration.

```
% ./gradlew generateSwaggerCodeHelp
:generateSwaggerCodeHelp
Available JSON configuration for task ':generateSwaggerCode':

CONFIG OPTIONS
	sortParamsByRequiredFlag
  ...
```


Getting Started: Document Generation
------------------------------------

Create following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '1.2.1'
  id 'org.asciidoctor.convert' version '1.5.3'
}

repositories {
  jcenter()
}

generateSwaggerDoc {
  inputFile = file('petstore.yaml')
}
```

The task generates an API document as `build/swagger-doc/index.html`.

```
% ./gradlew generateSwaggerDoc
:generateSwaggerDoc_swagger2markup
:generateSwaggerDoc_asciidoctor
:generateSwaggerDoc
```


Custom code generation
----------------------

The plugin supports a custom template and custom generator class.
See projects under [acceptance-test](acceptance-test) for more.

Task type `GenerateSwaggerCode` accepts below properties.

Key           | Type              | Value                                   | Example value
--------------|-------------------|-----------------------------------------|--------------
`language`    | String, required  | Language to generate                    | `spring`
`library`     | String, optional  | Library                                 | `spring-mvc`
`inputFile`   | File, required    | Swagger spec file                       | [`file('petstore.yaml')`](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/yaml/petstore.yaml)
`outputDir`   | File, optional    | Directory to write generated files. Defaults to `$buildDir/swagger-code`. | `file('petstore')`
`configFile`  | File, optional    | [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator) | `file('config.json')`
`templateDir` | File, optional    | Directory containing the template       | `file('src/template')`
`components`  | List of Strings   | [Components to generate](https://github.com/swagger-api/swagger-codegen#selective-generation) that is a list of `models`, `apis` and `supportingFiles`. Defaults to all components | `['models', 'apis']`

Since the task type is a [`JavaExec` task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html),
it accepts `JavaExec` properties such as `classpath` or `systemProperties`.


Custom document generation
--------------------------

Task type `SwaggerDocgen` accepts below properties.

Key           | Type              | Value                                   | Example value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File, required    | Swagger spec file                       | [`file('petstore.yaml')`](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/yaml/petstore.yaml)
`outputDir`   | File, optional    | Directory to write the generated file. Defaults to `$buildDir/swagger-doc`. | `file('petstore')`
`config`      | Map, optional     | [Configuration of Swagger2Markup](http://swagger2markup.github.io/swagger2markup/1.1.0/#_swagger2markup_properties) | `swagger2markup.pathsGroupedBy: 'TAGS'`

See projects under [acceptance-test](acceptance-test) for more.


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
