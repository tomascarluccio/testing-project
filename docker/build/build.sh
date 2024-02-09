#!/bin/bash

TAG=${1:-latest}

docker pull ubuntu:22.04
docker build  -t altipeak/safewalk-mt-suite-test:$TAG  -f Dockerfile .
