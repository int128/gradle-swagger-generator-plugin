package org.hidetake.gradle.swagger.generator.codegen

import java.util.zip.ZipFile

@Singleton
class DefaultAdaptorFactory implements AdaptorFactory {
    @Override
    Adaptor findAdaptor(Set<File> generatorFiles) {
        if (findClass(Swagger2Adaptor.CLASS_NAME, generatorFiles)) {
            return new Swagger2Adaptor()
        }
        if (findClass(Swagger3Adaptor.CLASS_NAME, generatorFiles)) {
            return new Swagger3Adaptor()
        }
        if (findClass(OpenAPI3Adaptor.CLASS_NAME, generatorFiles)) {
            return new OpenAPI3Adaptor()
        }
        null
    }

    static boolean findClass(String className, Set<File> generatorFiles) {
        def classFileName = className.replace('.', '/') + '.class'
        Helper.findJARs(generatorFiles).find { f ->
            if (!f.name.endsWith('.jar')) {
                return false
            }
            new ZipFile(f).entries().find { e ->
                e.name == classFileName
            }
        }
    }
}
