#!/bin/bash
set -ev

./gradlew clean
./gradlew assemble
./gradlew bintrayUpload -PdryRun=true

./gradlew :debugdrawer:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-action:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-base:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-commons:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-no-op:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-view:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-view-no-op:bintrayUpload -PdryRun=false

./gradlew :debugdrawer-fps:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-glide:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-location:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-okhttp3:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-okhttp:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-picasso:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-scalpel:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-timber:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-logs:bintrayUpload -PdryRun=false
./gradlew :debugdrawer-network-quality:bintrayUpload -PdryRun=false
