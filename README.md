# Gradle Swagger Generator Plugin [![build](https://github.com/int128/gradle-swagger-generator-plugin/actions/workflows/build.yaml/badge.svg)](https://github.com/int128/gradle-swagger-generator-plugin/actions/workflows/build.yaml) [![Gradle Status](https://gradleupdate.appspot.com/int128/gradle-swagger-generator-plugin/status.svg)](https://gradleupdate.appspot.com/int128/gradle-swagger-generator-plugin/status)

## Table of content

- [Introduction](#introduction)
- [Code Generation](#code-generation)
- [Document Generation](#document-generation)
  - [Swagger UI](#swagger-ui)
  - [ReDoc](#redoc)
  - [HTML](#html)
- [Recipes](#recipes)
  - [Use configuration file](#use-configuration-file)
  - [Build generated code](#build-generated-code)
  - [Validate YAML before code generation](#validate-yaml-before-code-generation)
  - [Selective generation](#selective-generation)
  - [Use custom template](#use-custom-template)
  - [Use custom generator class](#use-custom-generator-class)
  - [Externalize template or generator class](#externalize-template-or-generator-class)
  - [Use multiple sources](#use-multiple-sources)
  - [Switch version of Swagger Codegen](#switch-version-of-swagger-codegen)
  - [Configure Swagger UI](#configure-swagger-ui)
- [Compatibility](#compatibility)
  - [Swagger Codegen v3 and Java 16+](#swagger-codegen-v3-and-java-16)
- [Settings](#settings)
  - [Task type `ValidateSwagger`](#task-type-validateswagger)
  - [Task type `GenerateSwaggerCode`](#task-type-generateswaggercode)
  - [Task type `GenerateSwaggerUI`](#task-type-generateswaggerui)
  - [Task type `GenerateReDoc`](#task-type-generateredoc)
- [Contributions](#contributions)


## Introduction

This is a Gradle plugin for the following tasks:

- Validate an OpenAPI YAML.
- Generate source from an OpenAPI YAML using [Swagger Codegen v2/v3](https://github.com/swagger-api/swagger-codegen) and [OpenAPI Generator v3](https://github.com/OpenAPITools/openapi-generator).
- Generate Swagger UI with an OpenAPI YAML.
- Generate ReDoc with an OpenAPI YAML.

See also the following examples:

- [Swagger UI example](https://int128.github.io/gradle-swagger-generator-plugin/examples/ui-v3/) (generated by [ui-v3/basic](/acceptance-test/projects/ui-v3/basic/build.gradle) project)
- [ReDoc example](https://int128.github.io/gradle-swagger-generator-plugin/examples/redoc/) (generated by [redoc/basic](/acceptance-test/projects/redoc/basic/build.gradle) project)
- [HTML document example](https://int128.github.io/gradle-swagger-generator-plugin/examples/html-codegen-v2/) (generated by [codegen-v2/html](/acceptance-test/projects/codegen-v2/html/build.gradle) project)
- [HTML document example](https://int128.github.io/gradle-swagger-generator-plugin/examples/html-codegen-v3/) (generated by [codegen-v3/html](/acceptance-test/projects/codegen-v3/html/build.gradle) project)
- [HTML document example](https://int128.github.io/gradle-swagger-generator-plugin/examples/html-openapi-v3/) (generated by [openapi-v3/html](/acceptance-test/projects/openapi-v3/html/build.gradle) project)


## Code Generation

Create a project with the following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '2.19.2'
}

repositories {
  mavenCentral()
}

dependencies {
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.4.34'             // Swagger Codegen V2
  swaggerCodegen 'io.swagger.codegen.v3:swagger-codegen-cli:3.0.47'  // or Swagger Codegen V3
  swaggerCodegen 'org.openapitools:openapi-generator-cli:3.3.4'     // or OpenAPI Generator
}

swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
    }
  }
}
```

The task generates source code into `build/swagger-code-petstore`.

```
% ./gradlew generateSwaggerCode
:resolveSwaggerTemplate NO-SOURCE
:generateSwaggerCodePetstore
:generateSwaggerCode NO-SOURCE
```


## Document Generation

### Swagger UI

Create a project with the following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '2.19.2'
}

repositories {
  mavenCentral()
}

dependencies {
  swaggerUI 'org.webjars:swagger-ui:3.52.5'
}

swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
  }
}
```

The task generates an API document as `build/swagger-ui-petstore`.

```
% ./gradlew generateSwaggerUI
:generateSwaggerUIPetstore
:generateSwaggerUI NO-SOURCE
```


### ReDoc

Create a project with the following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '2.19.2'
}

swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
  }
}
```

The task generates an API document as `build/redoc-petstore`.

```
% ./gradlew generateReDoc
:generateReDocPetstore
:generateReDoc NO-SOURCE
```


### HTML

Create a project with the following build script.

```groovy
plugins {
  id 'org.hidetake.swagger.generator' version '2.19.2'
}

repositories {
  mavenCentral()
}

dependencies {
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.4.34'             // Swagger Codegen V2
  swaggerCodegen 'io.swagger.codegen.v3:swagger-codegen-cli:3.0.47'  // or Swagger Codegen V3
}

swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'html'  // html or html2
    }
  }
}
```

The task generates a static HTML into `build/swagger-code-petstore`.

```
% ./gradlew generateSwaggerCode
:resolveSwaggerTemplate NO-SOURCE
:generateSwaggerCodePetstore
:generateSwaggerCode NO-SOURCE
```


## Recipes

See the example projects in [acceptance-test](acceptance-test).

### Use configuration file

We can use a [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator) as follows:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
      configFile = file('config.json')
    }
  }
}
```

`config.json` depends on the language and framework. For example,

```json
{
  "library": "spring-mvc",
  "modelPackage": "example.model",
  "apiPackage": "example.api",
  "invokerPackage": "example"
}
```

Run the task with `Help` postfix to show available JSON configuration.

```
% ./gradlew generateSwaggerCodePetstoreHelp
:generateSwaggerCodePetstoreHelp
=== Available raw options
NAME
        swagger-codegen-cli generate - Generate code with chosen lang

SYNOPSIS
        swagger-codegen-cli generate
                [(-a <authorization> | --auth <authorization>)]
...

=== Available JSON configuration for language spring:

CONFIG OPTIONS
	sortParamsByRequiredFlag
...
```


### Build generated code

It is recommended to generate code into an ephemeral directory (e.g. `build`) and exclude it from a Git repository.
We can compile generated code as follows:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
      configFile = file('config.json')
    }
  }
}

// Configure compile task dependency and source
compileJava.dependsOn swaggerSources.petstore.code
sourceSets.main.java.srcDir "${swaggerSources.petstore.code.outputDir}/src/main/java"
sourceSets.main.resources.srcDir "${swaggerSources.petstore.code.outputDir}/src/main/resources"
```

See also the following examples:

- [codegen-v2/java-spring](acceptance-test/projects/codegen-v2/java-spring) 
- [codegen-v3/java-spring](acceptance-test/projects/codegen-v3/java-spring)
- [openapi-v3/java-spring](acceptance-test/projects/openapi-v3/java-spring)


### Validate YAML before code generation

It is recommended to validate an OpenAPI YAML before code generation in order to avoid invalid code generated.

If you use OpenAPI Generator as generator, YAML validation is embeding.

We can validate a YAML as follows:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
      configFile = file('config.json')
      // Validate YAML before code generation. for Swagger Codegen V2 / V3
      dependsOn validation
    }
  }
}
```


### Selective generation

We can control output of code generation.
At default everything is generated but only models and APIs are generated in the following:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
      configFile = file('config.json')
      // Generate only models and controllers
      components = ['models', 'apis']
    }
  }
}
```

`components` property accepts a list of strings or map.

```groovy
// generates only models
components = ['models']
components = [models: true]

// generate only User and Pet models
components = [models: ['User', 'Pet']]
components = [models: 'User,Pet']

// generate only APIs (without tests)
components = [apis: true, apiTests: false]
components = [apis: true, apiTests: null]
```

See [selective generation section](https://github.com/swagger-api/swagger-codegen#selective-generation) for details.


### Use custom template

We can use a custom template for the code generation as follows:

```groovy
// build.gradle
swaggerSources {
  inputFile = file('petstore.yaml')
  petstore {
    language = 'spring'
    // Path to the template directory
    templateDir = file('templates/spring-mvc')
  }
}
```

See also the following examples:

- [codegen-v2/custom-template](acceptance-test/projects/codegen-v2/custom-template)
- [codegen-v3/custom-template](acceptance-test/projects/codegen-v3/custom-template)
- [openapi-v3/custom-template](acceptance-test/projects/openapi-v3/custom-template)


### Use custom generator class

We can use a custom generator class for the code generation as follows:

```groovy
// build.gradle
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      // FQCN of the custom generator class
      language = 'CustomGenerator'
    }
  }
}

dependencies {
  swaggerCodegen project('generators')
}

swaggerSources*.code*.dependsOn 'generators:jar'
```

```groovy
// generators/build.gradle (child project)
dependencies {
  implementation 'io.swagger:swagger-codegen-cli:2.4.24'
}
```

```groovy
// generators/src/main/groovy/CustomGenerator.groovy
import io.swagger.codegen.languages.SpringCodegen

class CustomGenerator extends SpringCodegen {
}
```

See also the following examples:

- [codegen-v2/custom-class](acceptance-test/projects/codegen-v2/custom-class)
- [codegen-v3/custom-class](acceptance-test/projects/codegen-v3/custom-class)
- [openapi-v3/custom-class](acceptance-test/projects/openapi-v3/custom-class)


### Externalize template or generator class

In some large use case, we can release a template or generator to an external repository and use them from projects.

```groovy
// build.gradle
repositories {
  // Use external repository for the template and the generator class
  maven {
    url 'https://example.com/nexus-or-artifactory'
  }
  mavenCentral()
}

dependencies {
  swaggerCodegen 'io.swagger:swagger-codegen-cli:2.4.34'
  // Add dependency for the template
  swaggerTemplate 'com.example:swagger-templates:1.0.0'
  // Add dependency for the generator class
  swaggerCodegen 'com.example:swagger-generators:1.0.0'
}

swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'spring'
      // The plugin automatically extracts template JAR into below destination
      templateDir = file("${resolveSwaggerTemplate.destinationDir}/spring-mvc")
    }
  }
}
```

See also the following examples:

- [codegen-v2/externalize-template](acceptance-test/projects/codegen-v2/externalize-template) and [codegen-v2/externalize-class](acceptance-test/projects/codegen-v2/externalize-class)
- [codegen-v3/externalize-template](acceptance-test/projects/codegen-v3/externalize-template) and [codegen-v3/externalize-class](acceptance-test/projects/codegen-v3/externalize-class)
- [openapi-v3/externalize-template](acceptance-test/projects/openapi-v3/externalize-template) and [openapi-v3/externalize-class](acceptance-test/projects/openapi-v3/externalize-class)



### Use multiple sources

We can handle multiple sources in a project as follows:

```groovy
// build.gradle
swaggerSources {
    petstoreV1 {
        inputFile = file('v1-petstore.yaml')
        code {
            language = 'spring'
            configFile = file('v1-config.json')
        }
    }
    petstoreV2 {
        inputFile = file('v2-petstore.yaml')
        code {
            language = 'spring'
            configFile = file('v2-config.json')
        }
    }
}

compileJava.dependsOn swaggerSources.petstoreV1.code, swaggerSources.petstoreV2.code
sourceSets.main.java.srcDirs "${swaggerSources.petstoreV1.code.outputDir}/src/main/java", "${swaggerSources.petstoreV2.code.outputDir}/src/main/java"
sourceSets.main.resources.srcDirs "${swaggerSources.petstoreV1.code.outputDir}/src/main/resources", "${swaggerSources.petstoreV2.code.outputDir}/src/main/resources"
```

See also the following examples:

- [codegen-v2/multiple-sources](acceptance-test/projects/codegen-v2/multiple-sources) 
- [codegen-v3/multiple-sources](acceptance-test/projects/codegen-v3/multiple-sources)
- [openapi-v3/multiple-sources](acceptance-test/projects/openapi-v3/multiple-sources)


### Switch version of Swagger Codegen

We can use multiple versions of Swagger Codegen as follows:

```groovy
// build.gradle
configurations {
    swaggerCodegenV2
    swaggerCodegenV3
}

dependencies {
    swaggerCodegenV2 'io.swagger:swagger-codegen-cli:2.4.24'
    swaggerCodegenV3 'io.swagger.codegen.v3:swagger-codegen-cli:3.0.30'
}

swaggerSources {
    petstoreV2 {
        inputFile = file('v2-petstore.yaml')
        code {
            language = 'spring'
            configuration = configurations.swaggerCodegenV2
        }
    }
    petstoreV3 {
        inputFile = file('v3-petstore.yaml')
        code {
            language = 'spring'
            configuration = configurations.swaggerCodegenV3
        }
    }
}
```

See also the following examples:

- [codegen-v3/multiple-codegen-versions](acceptance-test/projects/codegen-v3/multiple-codegen-versions)
- [openapi-v3/multiple-codegen-versions](acceptance-test/projects/openapi-v3/multiple-codegen-versions)


### Configure Swagger UI

We can [configure Swagger UI](https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md)
by overwriting [the default `index.html`](src/main/resources/swagger-ui.html) as follows:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    ui {
      doLast {
        copy {
          from 'index.html'
          into outputDir
        }
      }
    }
  }
}
```

You can create an `index.html` from [the Swagger UI official one](https://github.com/swagger-api/swagger-ui/blob/master/dist/index.html).
It must satisfy the followings:

- Put `<script src="./swagger-spec.js">` in order to load a Swagger spec.
  The plugin exports the Swagger spec as `swagger-spec.js` file while generation.
- Set `spec: window.swaggerSpec` in `SwaggerUIBundle()` parameters.
- Set `validatorUrl: null` in `SwaggerUIBundle()` parameters in order to turn off the validator badge.

See also the following examples:

- [ui-v3/basic](acceptance-test/projects/ui-v3/basic)
- [ui-v2/basic](acceptance-test/projects/ui-v2/basic) (if you need Swagger UI 2.x)


## Compatibility

### Swagger Codegen v3 and Java 16+

To use Swagger Codegen v3 on Java 16 or later, you need to set `jvmArgs` as follows:

```groovy
swaggerSources {
  petstore {
    inputFile = file('petstore.yaml')
    code {
      language = 'html'
      jvmArgs = ['--add-opens=java.base/java.util=ALL-UNNAMED'] // for Swagger Codegen v3 on Java 16+
    }
  }
}
```

See [#221](https://github.com/int128/gradle-swagger-generator-plugin/issues/221) for details.


## Settings

The plugin adds `validateSwagger`, `generateSwaggerCode`, `generateSwaggerUI` and `GenerateReDoc` tasks.
A task will be skipped if no input file is given.


### Task type `ValidateSwagger`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`reportFile`  | File              | File to write validation report.        | `$buildDir/tmp/validateSwagger/report.yaml`

It depends on the following JSON schema:

- [OpenAPI Specification version 2.0](https://github.com/OAI/OpenAPI-Specification/blob/master/schemas/v2.0/schema.json)


### Task type `GenerateSwaggerCode`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`language`    | String            | Language to generate.                   | Mandatory
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write generated files.     | `$buildDir/swagger-code`
`wipeOutputDir` | Boolean         | Wipe the `outputDir` before generation. | `true`
`library`     | String            | Library type.                           | None
`configFile`  | File              | [JSON configuration file](https://github.com/swagger-api/swagger-codegen#customizing-the-generator). | None
`templateDir` | File              | Directory containing the template.      | None
`components`  | List or Map       | [Components to generate](https://github.com/swagger-api/swagger-codegen#selective-generation) that is a list of `models`, `apis` and `supportingFiles`. | All components
`additionalProperties` | Map of String, String | [Additional properties](https://github.com/swagger-api/swagger-codegen#to-generate-a-sample-client-library). | None
`rawOptions`  | List of Strings   | Raw command line options for Swagger Codegen | None
`configuration` | String or Configuration | Configuration for Swagger Codegen | `configurations.swaggerCodegen`
`jvmArgs`     | List of Strings   | Arguments passed to jvm | None


### Task type `GenerateSwaggerUI`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write Swagger UI files.    | `$buildDir/swagger-ui`
`wipeOutputDir` | Boolean         | Wipe the `outputDir` before generation. | `true`

Note that `options` and `header` are no longer supported since 2.10.0.
See the [Migration Guide](https://github.com/int128/gradle-swagger-generator-plugin/issues/81) for details.


### Task type `GenerateReDoc`

The task accepts below properties.

Key           | Type              | Value                                   | Default value
--------------|-------------------|-----------------------------------------|--------------
`inputFile`   | File              | Swagger spec file.                      | Mandatory
`outputDir`   | File              | Directory to write ReDoc files.         | `$buildDir/swagger-redoc`
`wipeOutputDir` | Boolean         | Wipe the `outputDir` before generation. | `true`
`scriptSrc`   | String            | URL to ReDoc JavaScript.                | `//rebilly.github.io/ReDoc/releases/latest/redoc.min.js`
`title`       | String            | HTML title.                             | `ReDoc - $filename`
`options`     | Map of Strings    | [ReDoc tag attributes](https://github.com/Rebilly/ReDoc#redoc-tag-attributes). | Empty map


## Contributions

This is an open source software licensed under the Apache License Version 2.0.
Feel free to open issues or pull requests.

CI requires the following variables.

Environment Variable        | Purpose
----------------------------|--------
`$GRADLE_PUBLISH_KEY`       | Publish the plugin to Gradle Plugins
`$GRADLE_PUBLISH_SECRET`    | Publish the plugin to Gradle Plugins
