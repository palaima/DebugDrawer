#!/bin/bash
set -ev

./gradlew clean
./gradlew assemble
./gradlew bintrayUpload
