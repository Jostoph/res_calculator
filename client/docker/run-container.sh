#!/bin/bash

# run container in calculator network
docker run -p 2019:2019 -i --network calculator_server_network java-calculator-client
