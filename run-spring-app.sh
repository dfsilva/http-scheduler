#!/bin/bash
export SPRING_PROFILES_ACTIVE=dev
echo $(date) + ' - Will try to run with profile ' + $SPRING_PROFILES_ACTIVE
chmod +x gradlew
./gradlew bootRun -Pdev