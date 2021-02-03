#!/bin/bash
./gradlew bootJar
cp build/libs/http-scheduler.jar docker/