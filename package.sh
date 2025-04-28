#!/bin/bash

# Clean the project, removing any previously compiled files and build artifacts
mvn clean

# Compile the project's source code
mvn compile

# Run the unit tests to ensure code correctness and functionality
mvn test
