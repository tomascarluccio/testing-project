#!/bin/bash
set -euo pipefail


f_clone_repository() {
    echo ">> Cloning repository <<"
    source /app/utilities/clone_repository.sh
    clone_repository
    echo "OK"
}

f_add_tests() {
	echo ">> Adding tests <<"
	source /app/utilities/add_tests.sh
	add_tests
    echo "OK"
}

f_run_tests() {
    echo ">> Running tests <<"
#    cd safewalk-testing && ./run_tests.sh --target=$TARGET 
    cd safewalk-testing && ./run_tests_v2.sh 
    echo "OK"
}

f_send_reports() {
	echo ">> Sending report <<"
	source /app/utilities/send_report.sh
	f_send_report
	cd /app
	tree .
    echo "OK"
}


main() {
    f_clone_repository
    f_add_tests
    f_run_tests
    f_send_reports
    wait
    echo "All tasks completed."
}

# Call the main function or command passed from console
main
