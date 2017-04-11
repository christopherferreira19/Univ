usage() {
    echo "test-run.sh <step> <rs count>"
}

if [[ -z "$1" ]]; then
    echo "Missing first argument"
    usage
    exit 1
fi

if [[ -z "$2" ]]; then
    echo "Missing second argument"
    usage
    exit 1
fi

# Setup the managed directories
NACHOS_BIN=$(check_file -e "$CODE_DIR/build/nachos-$1")
NACHOS_OPTION_RS_COUNT="$2"

IN_DIR=$(check_file -e $TEST_DIR/step2/data)
OUT_DIR=$(check_file -f $TEST_DIR/step2/out)

rm -rf $OUT_DIR
mkdir -p $OUT_DIR

do_diff() {
    diff $2 $1 1>/dev/null 2>&1
    rc=$?
    if [[ $rc -eq 0 ]]; then
        log "  Diff:   \e[32mOk !\e[39m"
        echo -en "\e[32m.\e[39m"
    else
        log "  Diff:   \e[31mFailed !\e[39m"
        diff -d $DIFF_COLOR $1 $2 >> $LOGFILE 2>&1
        FAILED=$(($FAILED + 1))
        echo -en "\e[31mF\e[39m"
    fi
    return $rc
}

TEST=""
for file in $CODE_DIR/test/step2/test*.c; do
    TEST="$TEST $(basename $file .c)"
done


for test in $TEST; do
    nachos_exe="$BUILD_DIR/step2/$test"
    linux_exe="$BUILD_DIR/step2/$test-linux"

    ls $IN_DIR/$test[-_]*.in 1>/dev/null 2>&1
    if [[ $? -ne 0 ]]; then
        out="$OUT_DIR/$test.$1.out"
        err="$OUT_DIR/$test.$1.err"
        linux_out="$out.$1.linux"
        log "\e[94m### Running test \e[33m$test\e[39m"
        log -n "  Linux:  "
        $( $linux_exe > $linux_out; exit $? )
        log_result
        if [[ $? -ne 0 ]]; then continue; fi

        log "  Nachos:  "
        for rsi in $(seq 1 $NACHOS_OPTION_RS_COUNT); do
            rs=$RANDOM
            nachos_out="$out.nachos.$rs"
            nachos_err="$err.nachos.$rs"
            log -n "    with (-rs=$rs) : "
            $( $NACHOS_BIN -x $nachos_exe -o $nachos_out -rs $rs >$nachos_err 2>&1; exit $? )
            log_result
            if [[ $? -ne 0 ]]; then continue; fi
            do_diff $linux_out $nachos_out
        done

        log ""
    else
        # Test for each input
        for input in $IN_DIR/$test[-_]*.in; do
            out="$OUT_DIR/$(basename $input .in).$1.out"
            err="$OUT_DIR/$(basename $input .in).$1.err"
            linux_out="$out.$1.linux"
            log "\e[94m### Running \e[33m$test\e[39m"
            log "\e[94m### with input \e[33m$input\e[39m"
            log -n "  Linux:  "
            $( $linux_exe < $input > $linux_out; exit $? )
            log_result
            if [[ $? -ne 0 ]]; then continue; fi

            log "  Nachos : "
            for rsi in $(seq 1 $NACHOS_OPTION_RS_COUNT); do
                rs=$RANDOM
                nachos_out="$out.nachos.$rs"
                nachos_err="$err.nachos.$rs"
                log -n "    with (-rs=$rs) : "
                $( $NACHOS_BIN -x $nachos_exe -i $input -o $nachos_out -rs $rs >$nachos_err 2>&1; exit $? )
                log_result
                if [[ $? -ne 0 ]]; then continue; fi
                do_diff $linux_out $nachos_out
            done

            log ""
        done
    fi
done
