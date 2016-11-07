Example Projects
================

This contains example projects to verify behavior of the plugin.

Project: code-generator
-----------------------

This is a simple project to generate API server code and build it as a JAR.
See [the build script](code-generator/build.gradle) for more.

```
% ./gradlew build
:generateSwaggerCode
[main] INFO io.swagger.parser.Swagger20Parser - reading from ...
:compileJava
:processResources UP-TO-DATE
:classes
:jar
:assemble
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL
```


Project: html-generator
-----------------------

This is a simple project to generate a HTML file of API document.
See [the build script](html-generator/build.gradle) for more.

```
% ./gradlew generateSwaggerDoc
:generateSwaggerDoc_swagger2markup
:generateSwaggerDoc_asciidoctor
:generateSwaggerDoc

BUILD SUCCESSFUL
```


Project: custom-code-generator
------------------------------

This project uses the custom generator class.
See [the build script](custom-code-generator/build.gradle) for more.
