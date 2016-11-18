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


Project: doc-generator
----------------------

This is a simple project to generate a HTML file of API document.
See [the build script](doc-generator/build.gradle) for more.

```
% ./gradlew build
:assemble UP-TO-DATE
:check UP-TO-DATE
:generateSwaggerDoc_swagger2markup
:generateSwaggerDoc_asciidoctor
:generateSwaggerDoc UP-TO-DATE
:build UP-TO-DATE

BUILD SUCCESSFUL
```


Project: custom-code-generator
------------------------------

This project uses the custom generator class.
See [the build script](custom-code-generator/build.gradle) for more.


Project: publish-s3
-------------------

This project publishes the API client JAR and the API document into the S3 bucket.

```
% export AWS_ACCESS_KEY_ID=...
% export AWS_SECRET_ACCESS_KEY=...
% ./gradlew publish
```

In order to serve the API document on the web site,
the static web hosting and following bucket policy should be enabled on S3.

```javascript
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Sid": "AddPerm",
			"Effect": "Allow",
			"Principal": "*",
			"Action": [
				"s3:GetObject"
			],
			"Resource": [
				"arn:aws:s3:::gradle-swagger-generator-plugin/*"
			]
		}
	]
}
```
