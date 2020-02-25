.PHONY: all release-gh-pages release-plugin

all:
	./gradlew build

release-gh-pages:
	./gradlew acceptance-test:example-docs
	ghcp commit -u "$(CIRCLE_PROJECT_USERNAME)" -r "$(CIRCLE_PROJECT_REPONAME)" -b gh-pages -m "Published by $(CIRCLE_BUILD_URL)" -C acceptance-test/build examples

BUMP_VERSION_FILES := README.md
BUMP_VERSION_FILES += template-project/build.gradle

release-plugin:
	./gradlew publishPlugins
	# create a pull reqeust to bump the version
	sed -i -e "s/version '[0-9.]*'/version '$(CIRCLE_TAG)'/g" $(BUMP_VERSION_FILES)
	ghcp commit -u "$(CIRCLE_PROJECT_USERNAME)" -r "$(CIRCLE_PROJECT_REPONAME)" -b "bump-$(CIRCLE_TAG)" -m "Bump the version to $(CIRCLE_TAG)" $(BUMP_VERSION_FILES)
	ghcp pull-request -u "$(CIRCLE_PROJECT_USERNAME)" -r "$(CIRCLE_PROJECT_REPONAME)" -b "bump-$(CIRCLE_TAG)" --title "Bump the version to $(CIRCLE_TAG)"
