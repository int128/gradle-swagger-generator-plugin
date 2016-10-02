import io.swagger.codegen.languages.SpringCodegen

class MySpringCodegen extends SpringCodegen {

    @Override
    void processOpts() {
        super.processOpts()

        assert supportingFiles instanceof List
        supportingFiles.removeIf { supportingFile ->
            !(supportingFile.folder =~ '^src/main/java')
        }
    }

}
