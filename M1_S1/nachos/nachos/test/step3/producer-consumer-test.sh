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
NACHOS_PC=$(check_file -e "$CODE_DIR/build/step3/producer-consumer$3")
NACHOS_OPTION_RS_COUNT="$2"

IN_DIR=$(check_file -e $TEST_DIR/step3/data)
OUT_DIR=$(check_file -f $TEST_DIR/step3/out)

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
        diff -d $DIFF_COLOR $1 $2 >> $LOGFILE
        FAILED=$(($FAILED + 1))
        echo -en "\e[31mF\e[39m"
    fi
    return $rc
}

for input in $IN_DIR/pc-*.in; do
    ref="$OUT_DIR/$(basename $input .in).$1.ref"
    out="$OUT_DIR/$(basename $input .in).$1.out"
    srt="$OUT_DIR/$(basename $input .in).$1.srt"
    err="$OUT_DIR/$(basename $input .in).$1.err"
    log "\e[94m### Running \e[33mProducer Consumer\e[39m"
    log "\e[94m### with input \e[33m$input\e[39m"

    sed -n 3~2p $input | sort -n > $ref

    log "Nachos: "
    for rsi in $(seq 1 $NACHOS_OPTION_RS_COUNT); do
        rs=$RANDOM
        nachos_out="$out.nachos.$rs"
        nachos_srt="$srt.nachos.$rs"
        nachos_err="$err.nachos.$rs"
        log -n "    with (-rs=$rs) : "
        $( $NACHOS_BIN -x $NACHOS_PC -i $input -o $nachos_out -rs $rs >/dev/null 2>$nachos_err; exit $? )
        log_result
        sort -n $nachos_out > $nachos_srt
        if [[ $? -ne 0 ]]; then continue; fi
        do_diff $ref $nachos_srt
    done
done