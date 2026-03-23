#!/bin/bash
./gradlew clean test jacocoTestReport checkstyleMain checkstyleTest check coverage
