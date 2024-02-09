#!/bin/bash

docker run --rm --env-file ./mt.env --name testing -it -v ./external_files:/app/external_files altipeak/safewalk-mt-suite-test:latest
