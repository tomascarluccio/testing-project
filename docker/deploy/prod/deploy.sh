#!/bin/bash

docker run --rm --env-file ./prod.env --name testing -it altipeak/safewalk-mt-suite-test
