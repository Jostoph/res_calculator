#!/bin/bash

echo "building server image..."
cd docker/
./build-image.sh
echo "done"
./run-container.sh
