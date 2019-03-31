#!/bin/bash

echo "building server image..."
cd docker/
./build-image.sh
echo "done"
echo "setting up network and running server container"
./run-container.sh
