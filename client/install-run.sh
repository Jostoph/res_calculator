#!/bin/bash

echo "building client image..."
cd docker/
./build-image.sh
echo "done"
echo "running client container"
./run-container.sh
