package org.hidetake.gradle.swagger.generator

import org.gradle.api.NamedDomainObjectContainer

/**
 * An extension class to provide declarative syntax.
 *
 * @author Hidetake Iwata
 */
class SwaggerGeneratorExtension {

    def SwaggerGeneratorExtension(NamedDomainObjectContainer<SwaggerSource> code) {
        this.code = code
    }

    final NamedDomainObjectContainer<SwaggerSource> code

    NamedDomainObjectContainer<SwaggerSource> code(@DelegatesTo(SwaggerSource) Closure configuration) {
        code.configure(configuration)
    }

}
