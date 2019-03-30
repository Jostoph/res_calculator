#!/bin/bash

mvn clean install --file ..app/pom.xml

cp ../app/target/ResCalculatorServer-1.0-SNAPSHOT-standalone.jar .

docker build --tag java-calculator-server .
