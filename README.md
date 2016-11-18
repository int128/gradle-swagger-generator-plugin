Gradle Swagger Generator Plugin [![Build Status](https://travis-ci.org/int128/gradle-swagger-generator-plugin.svg?branch=master)](https://travis-ci.org/int128/gradle-swagger-generator-plugin)
=============================

This is a Gradle plugin to generate server code, client code and API document, using [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) and [Swagger2Markup](https://github.com/Swagger2Markup/swagger2markup).

See also [example projects](acceptance-test/) and [the example of API document](https://int128.github.io/gradle-swagger-generator-plugin/).


Getting Started: Code Generation
--------------------------------

Create following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '1.5.1'
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

The task generates source code into `build/swagger-code`.

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
  id 'org.hidetake.swagger.generator' version '1.5.1'
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


Getting Started: Swagger UI Generation
--------------------------------------

Create following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '1.5.1'
}

repositories {
  jcenter()
}

dependencies {
    swaggerUI 'org.webjars:swagger-ui:2.2.6'
}

generateSwaggerUI {
  inputFile = file('petstore.yaml')
}
```

The task generates an API document as `build/swagger-ui`.

```
% ./gradlew generateSwaggerUI
:generateSwaggerUI
```


Custom code generation
----------------------

Task type `GenerateSwaggerCode` accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`language`    | String            | Language to generate.                   | Mandatory
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write generated files.     | `$buildDir/swagger-code`
`library`     | String            | Library type.                           | None
`configFile`  | File              | [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator). | None
`templateDir` | File              | Directory containing the template.      | None
`components`  | List of Strings   | [Components to generate](https://github.com/swagger-api/swagger-codegen#selective-generation) that is a list of `models`, `apis` and `supportingFiles`. | All components

The task supports a custom generator class by passing `classpath` property.
See projects under [acceptance-test](acceptance-test) for more.


Custom document generation
--------------------------

Task type `GenerateSwaggerDoc` accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write the generated file.  | `$buildDir/swagger-doc`
`config`      | Map               | [Configuration for Swagger2Markup](http://swagger2markup.github.io/swagger2markup/1.1.0/#_swagger2markup_properties). | None
`options`     | Map               | [Options for Asciidoctor Gradle Plugin](https://github.com/asciidoctor/asciidoctor-gradle-plugin#options--attributes). | None
`attributes`  | Map               | [Attributes for Asciidoctor Gradle Plugin](https://github.com/asciidoctor/asciidoctor-gradle-plugin#options--attributes). | `[toc: 'right', sectnums: '', sectanchors: '']`

Task type `GenerateSwaggerUI` accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write the generated file.  | `$buildDir/swagger-ui`

See projects under [acceptance-test](acceptance-test) for more.


Contributions
-------------

This is an open source software licensed under the Apache License Version 2.0.
Feel free to open issues or pull requests.

Travis CI builds the plugin continuously.
Following variables should be set.

Environment Variable        | Purpose
----------------------------|--------
`$GRADLE_PUBLISH_KEY`       | Publish the plugin to Gradle Plugins
`$GRADLE_PUBLISH_SECRET`    | Publish the plugin to Gradle Plugins
`$GITHUB_TOKEN`             | Publish the example of API document to GitHub Pages
