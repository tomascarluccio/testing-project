#!/bin/bash
#set -euo pipefail

f_send_report () {

    DIR_NAME=$(date +'%Y-%m-%d_%H-%M-%S')

    mkdir -p "/app/$DIR_NAME/ui"
    mkdir -p "/app/$DIR_NAME/troubleshooting"
    mkdir -p "/app/$DIR_NAME/postman/orchestrator"
    mkdir -p "/app/$DIR_NAME/postman/tenant"

    newVersion="${tnTargetVersion// /-}"
    newVersion="${newVersion//\(/}"
    newVersion="${newVersion//\)/}"

    export tnModifiedVersion="$newVersion"

    if [ "$PLATFORM" = "mainv5" ]; then
        # Do something for mainv5 platform
        if [ -f "/app/safewalk-testing/$reportName.html" ]; then
		mv /app/safewalk-testing/$reportName.html /app/$DIR_NAME/ui/ST-report-${tnModifiedVersion}-$(date +'%Y-%m-%d-%H%M').html
	        else
		echo "No $reportName.html file found"
	    fi
    elif [ "$PLATFORM" = "main" ]; then
        # Do something for main platform
        if [ -f "/app/safewalk-testing/$reportName.html" ]; then
		mv /app/safewalk-testing/$reportName.html /app/$DIR_NAME/ui/MT-report-${tnModifiedVersion}-$(date +'%Y-%m-%d-%H%M').html
	        else
		echo "No $reportName.html file found"
	    fi
    else
        echo "Invalid platform"
    fi


# moving API reports to the compressed folder

    if ls /app/API_Orchestrator-* 1> /dev/null 2>&1; then
        echo "API Orchestrator report exist"
        cp /app/API_Orchestrator-* /app/$DIR_NAME/postman/orchestrator/
    else
        echo "No API Orchestrator report found"
    fi
    if ls /app/API_Tenant-* 1> /dev/null 2>&1; then
        echo "API Tenant report exist"
        cp /app/API_Tenant-* /app/$DIR_NAME/postman/tenant
    else
        echo "No API Tenant report found"
    fi
    if ls /app/API_V5-* 1> /dev/null 2>&1; then
        echo "API Safewalk v5 report exist"
        cp /app/API_V5-* /app/$DIR_NAME/postman/tenant
    else
        echo "No API Safewalk v5 report exist"
    fi
# moving troubleshooting package to compressed folder
    if ls /app/download/troubleshooting_package* 1> /dev/null 2>&1; then
        echo "Troubleshooting package found"
	cp /app/download/troubleshooting_package* /app/$DIR_NAME/troubleshooting/
    else
        echo "Troubleshooting package not found"
    fi



chmod 600 /app/utilities/id_rsa
		cd /app
		scp -r -o StrictHostKeyChecking=no  -i /app/utilities/id_rsa $DIR_NAME/   altipeak@192.168.140.12:/home/altipeak/testing-web-console/reports/$DIR_NAME

	REPORT_COMPRESS_NAME=safewalk-testsuite-$DIR_NAME
	tar -cvf /app/$REPORT_COMPRESS_NAME.tar  --absolute-names /app/$DIR_NAME



	swaks --to "$RECIPIENTS" \
      	      --from "$SMTP_USER" \
      	      --header 'Subject: *** SAFEWALK MT Test Suite - Report ***' \
              --body "Report" \
              --attach-type text/html \
	          --attach @/app/$REPORT_COMPRESS_NAME.tar \
              --server "$SMTP_SERVER" \
              --port "$SMTP_PORT" \
              --auth-user "$SMTP_USER" \
              --auth-password "$SMTP_PASSWORD" \
              --tls \
		>/dev/null
}
