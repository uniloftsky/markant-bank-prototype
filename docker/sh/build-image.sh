#!/bin/bash

cd ..
./mvnw clean install

cd docker
docker build --rm --no-cache -t markant-bank -f ../Dockerfile ../boot/webapps