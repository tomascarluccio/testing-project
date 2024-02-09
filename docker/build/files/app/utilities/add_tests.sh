#!/bin/bash

add_tests() {

    superadmin_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/tenant/superadmin | grep -v / | grep -v LoginTest | sed 's/\.java//')
    authentication_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/tenant/authentication | grep -v / | grep -v 'LoginTest|PasswordRequiredTest|APISingleStepPushSign|API2StepVirtualOTP'  | sed 's/\.java//')
    managementconsole_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/tenant/managementconsole | grep -v / | grep -v LoginTest | sed 's/\.java//')
    selfserviceportal_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/tenant/selfserviceportal | grep -v / | grep -v LoginTest | sed 's/\.java//')
    postman_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/tenant/postman | grep -v / | grep -v LoginTest | sed 's/\.java//')
    orchestrator_list_of_tests=$(ls -p /app/safewalk-testing/src/main/java/tests/orchestrator | grep -v / | grep -v LoginTest | sed 's/\.java//')
    

    target_xml_file="/app/safewalk-testing/testng_tn.xml"


    if [[ "$SUPERADMIN_TESTS" == "true" ]]; then
        for class_name in $superadmin_list_of_tests; do
            echo "<class name=\"tests.tenant.superadmin.$class_name\"/>"
            sed -i "/<class name=\"tests.tenant.superadmin.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.tenant.superadmin.$class_name\"\/>" "$target_xml_file"
        done
    fi

    if [[ "$AUTHENTICATION_TESTS" == "true" ]]; then
        for class_name in $authentication_list_of_tests; do
            echo "<class name=\"tests.tenant.authentication.$class_name\"/>"
            sed -i "/<class name=\"tests.tenant.superadmin.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.tenant.authentication.$class_name\"\/>" "$target_xml_file"
        done
    fi

    if [[ "$MANAGEMENTCONSOLE_TESTS" == "true" ]]; then
        for class_name in $managementconsole_list_of_tests; do
            echo "<class name=\"tests.tenant.managementconsole.$class_name\"/>"
            sed -i "/<class name=\"tests.tenant.superadmin.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.tenant.managementconsole.$class_name\"\/>" "$target_xml_file"
        done
    fi

    if [[ "$SELFSERVICEPORTAL" == "true" ]]; then
        for class_name in $selfserviceportal_list_of_tests; do
            echo "<class name=\"tests.tenant.selfserviceportal.$class_name\"/>"
            sed -i "/<class name=\"tests.tenant.superadmin.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.tenant.selfserviceportal.$class_name\"\/>" "$target_xml_file"
        done
    fi

    if [[ "$POSTMAN_API_TEST" == "true" ]]; then
        for class_name in $postman_list_of_tests; do
            echo "<class name=\"tests.tenant.postman.$class_name\"/>"
            sed -i "/<class name=\"tests.tenant.superadmin.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.tenant.postman.$class_name\"\/>" "$target_xml_file"
        done
    fi


    target_xml_file="../safewalk-testing/testng_oc.xml"


    if [[ "$ORCHESTRATOR_TESTS" == "true" ]]; then
        for class_name in $orchestrator_list_of_tests; do
            echo "<class name=\"tests.tenant.superadmin.$class_name\"/>"
            sed -i "/<class name=\"tests.orchestrator.LoginTest\"\/>/a\ \ \ \ \ <class name=\"tests.orchestrator.$class_name\"\/>" "$target_xml_file"
        done
    fi

    if [[ "$DRY_MODE" == "true" ]]; then
	    cp /app/assets/testng_dry_run.xml /app/safewalk-testing/testng_tn.xml
    fi

    if [[ "$EXTERNAL_TESTNG_TN" == "true" ]]; then
	     cp /app/safewalk-testing/src/main/resources/testng.xml /app/safewalk-testing/testng_tn.xml
	    #cp /app/external_files/testng.xml /app/safewalk-testing/testng_tn.xml
    fi
}
