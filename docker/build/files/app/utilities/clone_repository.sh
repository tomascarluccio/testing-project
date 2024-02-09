#!/bin/bash

clone_repository() {
    # Check if the required environment variables are set
    if [[ -z "$GIT_USERNAME" || -z "$GIT_PASSWORD" || -z "$REPO_URL" || -z "$TARGET_DIR" || -z "$PLATFORM" ]]; then
        echo "Please set the following environment variables: GIT_USERNAME, GIT_PASSWORD, REPO_URL, TARGET_DIR, PLATFORM"
        exit 1
    fi

    # Set Git credentials
    git config --global credential.helper 'store --file ~/.git-credentials'
    echo -e "https://$GIT_USERNAME:$GIT_PASSWORD@$REPO_URL" >> ~/.git-credentials

    # Clone the repository
    git clone -b "$PLATFORM" "https://$GIT_USERNAME:$GIT_PASSWORD@$REPO_URL" "$TARGET_DIR"

    # Clean up
    rm ~/.git-credentials

    echo "Repository cloned from branch $PLATFORM into $TARGET_DIR"
}