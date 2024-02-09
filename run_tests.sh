#!/bin/bash -e

SCRIPT_PATH="${BASH_SOURCE[0]:-$0}";
BASE_PATH=$(dirname -- "$SCRIPT_PATH");


TARGET="TENANT"


while [ $# -gt 0 ]; do
	case "$1" in
	--help*)
		echo "The available parameters are:
        --target=[TENANT|ORCHESTRATOR|MOBILE]"
		exit 0
	;;
	--base-path=*)
		BASE_PATH="${1#*=}"
	;;
    --target=*)
		TARGET="${1#*=}"
	;;
    *)
		echo "Error: Invalid argument"
		exit 1
  	esac
  	shift
done


if [[ ! $TARGET =~ ^(TENANT|ORCHESTRATOR|MOBILE)$ ]]; then 
  echo "Argument --target must be TENANT, ORCHESTRATOR or MOBILE"
  exit 1
fi


TJAR_PATH=$BASE_PATH/target/safewalk_testing.jar
CONF_PATH=$BASE_PATH/src/main/resources/config.properties


cd $BASE_PATH

rm -rf $BASE_PATH/src/main/resources/testng.xml

if [ $TARGET = "TENANT" ]; then
	cp $BASE_PATH/testng_tn.xml $BASE_PATH/src/main/resources/testng.xml
if

if [ $TARGET = "ORCHESTRATOR" ]; then
	cp $BASE_PATH/testng_oc.xml $BASE_PATH/src/main/resources/testng.xml
if

if [ $TARGET = "MOBILE" ]; then
	cp $BASE_PATH/testng_mb.xml $BASE_PATH/src/main/resources/testng.xml
if

mvn compile -q
mvn package -q

java -jar $TJAR_PATH $CONF_PATH 

rm -rf $BASE_PATH/src/main/resources/testng.xml
