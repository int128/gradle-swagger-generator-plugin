#!/bin/bash
set -e
set -o pipefail
set -x
test "$CIRCLE_BUILD_URL"
test "$CIRCLE_REPOSITORY_URL"
test "$CIRCLE_USERNAME"

./gradlew acceptance-test:examples

mkdir -pv /tmp/gh-pages
cp -av ./acceptance-test/examples /tmp/gh-pages
cp -av ./.circleci /tmp/gh-pages

cd /tmp/gh-pages
git init
git checkout -b gh-pages
git add .
git config user.email "$CIRCLE_USERNAME@users.noreply.github.com"
git config user.name CircleCI
git commit -m "Published by CircleCI $CIRCLE_BUILD_URL"
git remote add origin "$CIRCLE_REPOSITORY_URL"
git push origin gh-pages -f
