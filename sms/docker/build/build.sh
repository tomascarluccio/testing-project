#!/bin/bash -e

TAG=${1:-latest}
BUILD_MODE=${2:-PRODUCTION}


rm -rf files/sms
cp -r ../../../sms files/sms
rm -rf files/sms/venv

docker pull debian:bullseye-slim

docker build --no-cache -t altipeak/sms:$TAG -f Dockerfile .

rm -rf files/sms
