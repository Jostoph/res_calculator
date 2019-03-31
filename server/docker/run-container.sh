#!/bin/bash

# create network for container communication
docker network inspect calculator_server_network &>/dev/null || 
    docker network create calculator_server_network

# run server in the created network
docker run --network-alias calculator_server --network calculator_server_network java-calculator-server
