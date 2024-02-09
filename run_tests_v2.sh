#!/bin/bash -e

# Define variables

SCRIPT_PATH="${BASH_SOURCE[0]:-$0}";
BASE_PATH=$(dirname -- "$SCRIPT_PATH");
CONF_PATH=$BASE_PATH/src/main/resources/config.properties
TJAR_PATH=$BASE_PATH/target/safewalk_testing.jar

# Initialize TARGET variable
TARGET=""

# Check if variables are valid
if [[ "$OC_UI" != "true" && "$OC_UI" != "false" ]]; then
    echo "Invalid value for OC_UI. Please set it to true or false."
    exit 1
fi

if [[ "$TNT_UI" != "true" && "$TNT_UI" != "false" ]]; then
    echo "Invalid value for TNT_UI. Please set it to true or false."
    exit 1
fi

if [[ "$AUTH_API" != "true" && "$AUTH_API" != "false" ]]; then
    echo "Invalid value for AUTH_API. Please set it to true or false."
    exit 1
fi

if [[ "$AUTH_RADIUS" != "true" && "$AUTH_RADIUS" != "false" ]]; then
    echo "Invalid value for AUTH_RADIUS. Please set it to true or false."
    exit 1
fi

if [[ "$POSTMAN_OC" != "true" && "$POSTMAN_OC" != "false" ]]; then
    echo "Invalid value for POSTMAN_OC. Please set it to true or false."
    exit 1
fi

if [[ "$POSTMAN_TNT" != "true" && "$POSTMAN_TNT" != "false" ]]; then
    echo "Invalid value for POSTMAN_TNT. Please set it to true or false."
    exit 1
fi


# Append tests to TARGET variable
if [[ "$OC_UI" == "true" ]]; then
    TARGET="oc-ui"
fi

if [[ "$TNT_UI" == "true" ]]; then
    if [[ -n "$TARGET" ]]; then
        TARGET="$TARGET tnt-ui"
    else
        TARGET="tnt-ui"
    fi
fi

if [[ "$AUTH_API" == "true" ]]; then
    if [[ -n "$TARGET" ]]; then
        TARGET="$TARGET auth-api"
    else
        TARGET="auth-api"
    fi
fi

if [[ "$AUTH_RADIUS" == "true" ]]; then
    if [[ -n "$TARGET" ]]; then
        TARGET="$TARGET auth-radius"
    else
        TARGET="auth-radius"
    fi
fi

if [[ "$POSTMAN_OC" == "true" ]]; then
    if [[ -n "$TARGET" ]]; then
        TARGET="$TARGET postman-oc"
    else
        TARGET="postman-oc"
    fi
fi

if [[ "$POSTMAN_TNT" == "true" ]]; then
    if [[ -n "$TARGET" ]]; then
        TARGET="$TARGET postman-tnt"
    else
        TARGET="postman-tnt"
    fi
fi

# Compile and run the test

mvn compile -q
mvn package -q

# run the test
java -jar $TJAR_PATH $CONF_PATH $TARGET
