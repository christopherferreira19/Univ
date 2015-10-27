#############################################################
# * Variables
#############################################################
COMPILER=clang

COMPILER_OPTIONS=-g --std=c11

SOURCE_FILES = \
	src/utils.c \
	src/easy_sort.c \
	src/quicksort.c

OBJECT_FILES = $(patsubst src/%.c,build/%.o,$(SOURCE_FILES))

DEPS_FILES = $(patsubst src/%.c,build/%.d,$(SOURCE_FILES))

#############################################################
# * Commands
#############################################################
.PHONY: all
all: \
	bin/test_sort \
	bin/measure_sort \
	bin/dutch_national_flag \
	bin/horner \
	bin/fibonnacci

.PHONY: clean
clean:
	@rm -rf {bin,build}

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
build/%.o: src/%.c | build
	$(COMPILER) $(COMPILER_OPTIONS) -c $< -o $@

bin/%: build/%.o $(OBJECT_FILES) | bin
	$(COMPILER) $(COMPILER_OPTIONS) -o $@ $^

#############################################################
# * C Header Dependencies Rules
#############################################################
build/%.d: src/%.c | build
	@echo -n '$@ build/' > $@
	@$(COMPILER) $(COMPILER_OPTIONS) -MM $^ >> $@

include $(DEPS_FILES)
