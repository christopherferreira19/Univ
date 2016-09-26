#############################################################
# * Variables
#############################################################
LIBELF_DIR = lib/elfutils-0.158_install
LIBELF_INC = $(LIBELF_DIR)/include
LIBELF_LIB = $(LIBELF_DIR)/lib

ELF_LINKER_DIR = lib/elf_linker-1.0
ELF_LINKER_OBJ = \
	$(ELF_LINKER_DIR)/arm_simulator_interface.o \
	$(ELF_LINKER_DIR)/csapp.o \
	$(ELF_LINKER_DIR)/debug.o \
	$(ELF_LINKER_DIR)/scanner.o \
	$(ELF_LINKER_DIR)/util.o \
	$(ELF_LINKER_DIR)/gdb_protocol.o

CC=gcc

CCOPTS=-g -pedantic -Wall -Werror --std=c99 -I$(LIBELF_INC) -I$(ELF_LINKER_DIR)

#############################################################
# * Files
#############################################################
SRC = \
	src/msg.c \
	src/endianess.c \
	src/elf_read.c \
	src/elf_common.c \
	src/elf_print.c \
	src/elf_relocate.c \
	src/elf_rel_args.c \
	src/elf_relocate_arm.c \
	src/elf_write.c

BSRC = \
	src/elf_reader.c \
	src/elf_relocation.c \
	src/arm_runner.c

TST = \
	test/test_lib.c \
	test/test_read.c \
	test/test_relocate.c

BTST = \
	test/test_runner.c

DEP += $(patsubst src/%.c,build/%.d,$(SRC))
DEP += $(patsubst src/%.c,build/%.d,$(BSRC))
DEP += $(patsubst test/%.c,build/%.d,$(TST))
DEP += $(patsubst test/%.c,build/%.d,$(BTST))

OBJ += $(patsubst src/%.c,build/src_%.o, $(SRC))
OBJ += $(patsubst test/%.c,build/test_%.o, $(TST))

BIN += $(patsubst src/%.c,bin/%,$(BSRC))
BIN += $(patsubst test/%.c,bin/%,$(BTST))

#############################################################
# * Objects files necessary for each binary
#############################################################
test_runner_OBJ += build/test_test_runner.o
test_runner_OBJ += $(OBJ)

elf_reader_OBJ = $(patsubst %,build/src_%.o, \
	msg \
	endianess \
	elf_common \
	elf_reader \
	elf_read \
	elf_print)

elf_relocation_OBJ = $(patsubst %,build/src_%.o, \
	msg \
	endianess \
	elf_common \
	elf_read \
	elf_write \
	elf_relocate \
	elf_relocate_arm \
	elf_rel_args \
	elf_relocation)

arm_runner_OBJ = $(patsubst %,build/src_%.o, \
	msg \
	endianess \
	elf_common \
	elf_read \
	arm_runner)
arm_runner_OBJ += $(ELF_LINKER_OBJ)

#############################################################
# * Commands
#############################################################
.PHONY: all
all: $(BIN)
	bin/test_runner

.PHONY: clean
clean:
	@rm -rf bin
	@rm -rf build

#############################################################
# * Directories Rules
#############################################################
bin:
	@mkdir -p bin

build:
	@mkdir -p build

#############################################################
# * C Compilation Rules
#############################################################
build/test_%.o: test/%.c | build
	$(CC) $(CCOPTS) -Isrc -c $< -o $@

build/src_%.o: src/%.c | build
	$(CC) $(CCOPTS) -c $< -o $@

bin/test_runner: $(test_runner_OBJ) | bin
	$(CC) $(CCOPTS) -o $@ $^ $(LIBELF_LIB)/libelf.so
# Idéalement devrait être mais bug sur les machines de l'UFR :
# bin/test_runner: $(test_runner_OBJ) | bin
# 	$(CC) $(CCOPTS) -L$(LIBELF_LIB) -lelf -o $@ $^

bin/elf_reader: $(elf_reader_OBJ) | bin
	$(CC) $(CCOPTS) -o $@ $^

bin/elf_relocation: $(elf_relocation_OBJ) | bin
	$(CC) $(CCOPTS) -o $@ $^

bin/arm_runner: $(arm_runner_OBJ) | bin
	$(CC) $(CCOPTS) -pthread -o $@ $^

#############################################################
# * C Dependencies Rules
#############################################################
build/test_%.d: test/test_%.c | build
	@echo -n '$@ build/' > $@
	@$(CC) $(CCOPTS) -Isrc -MM $< >> $@

build/%.d: src/%.c | build
	@echo -n '$@ build/' > $@
	@$(CC) $(CCOPTS) -MM $< >> $@

include $(DEP)
