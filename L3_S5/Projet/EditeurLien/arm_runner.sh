#!/bin/bash
MY_PATH=$(dirname $(readlink -f $0))
cd $MY_PATH

#LOCAL
SIMULATOR="lib/elf_linker-1.0/arm_simulator"
ARM_RUNNER="bin/arm_runner"
FILES_FOLDER="test/files"
LOG_DIR="log"
#REL
ELF_REL="bin/elf_relocation"
ELF_REL_OPT="-e main -s .text=0x20 -s .data=0x2800"
#FILES CMP
CMP_SUFFIXE="_to_cmp"

#FUNC
#=================================
init()
{
	mkdir -p log
	rm $FILES_FOLDER/example?$CMP_SUFFIXE
	rm ./$LOG_DIR/*	
}

log_it()
{

	echo "# $FILES_FOLDER/$1 > $LOG_DIR/$1.txt > $LOG_DIR/$1$CMP_SUFFIXE.txt"
	$SIMULATOR --gdb-port 7777 --trace-registers --trace-memory --trace-state > $LOG_DIR/$1.txt &
	sleep 1
	$ARM_RUNNER --host localhost --service 7777 $FILES_FOLDER/$1

	$SIMULATOR --gdb-port 7777 --trace-registers --trace-memory --trace-state > $LOG_DIR/$1$CMP_SUFFIXE.txt &
	sleep 1
	$ARM_RUNNER --host localhost --service 7777 $FILES_FOLDER/$1$CMP_SUFFIXE
}

elf_relocation ()
{
	$ELF_REL $ELF_REL_OPT $FILES_FOLDER/$1.o $FILES_FOLDER/$1$CMP_SUFFIXE
}

#=================================
#FILES
FILE1="example1"
FILE2="example2"
FILE4="example4"

#INIT
init

elf_relocation $FILE1
elf_relocation $FILE2
elf_relocation $FILE4

log_it $FILE1
log_it $FILE2
log_it $FILE4


#TEST
diff -q $LOG_DIR/$FILE1.txt $LOG_DIR/$FILE1$CMP_SUFFIXE.txt \
&& diff -q $LOG_DIR/$FILE2.txt $LOG_DIR/$FILE2$CMP_SUFFIXE.txt \
&& diff -q $LOG_DIR/$FILE4.txt $LOG_DIR/$FILE4$CMP_SUFFIXE.txt

if [ $? -ne 0 ]; then
	echo "ERREUR ARM RUNNER : Les fichiers sont différents"
fi

exit 0