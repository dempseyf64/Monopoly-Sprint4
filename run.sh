#!/bin/bash

# Clean, run tests, and package the application
mvn clean test package

# Run the compiled JAR file
java -jar target/monoepoly-game-1.0-SNAPSHOT.jar
