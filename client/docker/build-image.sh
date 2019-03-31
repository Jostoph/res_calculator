#!/bin/bash

# create jar file
mvn clean install --file ../app/pom.xml

# copy jar file to current folder
cp ../app/target/ResCalculatorClient-1.0-SNAPSHOT-standalone.jar .

# build image
docker build --tag java-calculator-client .
