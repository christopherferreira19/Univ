#! /bin/bash

check_file() {
    dir=$(readlink $1 $2)
    if [[ -z "$dir" ]]; then
        echo "Missing: $2" >&2
        exit 2
    else
        echo "$dir"
    fi
}

ROOT_DIR=$(check_file -e $(dirname $0)/../)
CODE_DIR=$(check_file -e $ROOT_DIR/code)
BUILD_DIR=$(check_file -e $CODE_DIR/build)
TEST_DIR=$(check_file -e $ROOT_DIR/test)
LOG_DIR=$(check_file -f $TEST_DIR/log)

# Check if diff supports the --color=always option
DIFF_COLOR=""
diff --color=always /dev/zero /dev/zero 1>/dev/null 2>&1
if [[ $? -eq 0 ]]; then
    DIFF_COLOR="--color=always"
fi

# Setup log
mkdir -p $LOG_DIR
LOGFILE="$LOG_DIR/$(date +'%d-%H.%M.%S').log"

log() {
    echo -e "$@" >> $LOGFILE
}

log_result() {
    rc=$?
    if [[ $rc -eq 0 ]]; then
        log "\e[32mOk !\e[39m"
    else
        log "\e[31mFailed !\e[39m"
        echo -en "\e[31mF\e[39m"
        FAILED=$(($FAILED + 1))
    fi
    return $rc
}

test_flavor() {
	if [[ -z $USER_FLAVORS ]]; then
		return 0
	fi

	case $USER_FLAVORS in
	*$1*)
		return 0
	esac

	return 1
}

log "\e[33m# Test\e[39m"
log "\e[33m# Date   : \e[94m$(date +'%a %d %b %Y at %H.%M.%S')\e[39m"
log "\e[33m# Commit : \e[94m$(git rev-parse HEAD)\e[39m"

FAILED=0

if test_flavor step2; then
	echo ""
	echo -ne "\e[33m# Step 2 : \e[39m"
	. step2/compare-linux-nachos.sh step2 1
fi

if test_flavor step3; then
	echo ""
	echo -ne "\e[33m# Step 3 : \e[39m"
	. step2/compare-linux-nachos.sh step3 10
	. step3/producer-consumer-test.sh step3 10
	. step3/producer-consumer-test.sh step3 10 _cond
fi

if test_flavor step4; then
	echo ""
	echo -ne "\e[33m# Step 4 : \e[39m"
	. step2/compare-linux-nachos.sh step4 10
	. step3/producer-consumer-test.sh step4 10
	. step3/producer-consumer-test.sh step4 10 _cond
fi

if test_flavor step6; then
	echo ""
	echo -ne "\e[33m# Step 6 : \e[39m"
	. step2/compare-linux-nachos.sh step6 10
	. step3/producer-consumer-test.sh step6 10
	. step3/producer-consumer-test.sh step6 10 _cond
fi

echo ""
if [[ $FAILED -gt 0 ]]; then
    echo ""
    echo -e "\e[31m/!\ \e[33m$FAILED failed tests \e[31m/!\ \e[39m"
    echo -e "\e[33mSee log in $LOGFILE\e[39m"
fi
echo ""

exit $FAILED
