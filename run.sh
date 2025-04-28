#!/bin/bash

# Clean the project and package it into a JAR file
mvn clean package

# Run the compiled JAR file
java -jar target/monopoly-game-1.0-SNAPSHOT.jar
