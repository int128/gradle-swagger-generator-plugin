#!/bin/bash
set -e
set -o pipefail
set -x
test "$CIRCLE_TAG"
test "$CIRCLE_REPOSITORY_URL"
test "$CIRCLE_USERNAME"

mkdir -pv /tmp/gradle-swagger-generator-plugin
cd /tmp/gradle-swagger-generator-plugin
git clone "$CIRCLE_REPOSITORY_URL" .
git checkout -b "verify-$CIRCLE_TAG"

sed -i -e "s/version '[0-9.]*'/version '$CIRCLE_TAG'/g" README.md template-project/build.gradle

git add .
git config user.email "$CIRCLE_USERNAME@users.noreply.github.com"
git config user.name CircleCI
git commit -m "Bump to $CIRCLE_TAG"
git push origin "verify-$CIRCLE_TAG"
