CODE=../../code
STEP2_BUILD_DIR=$(CODE)/build/step2
STEP2_SRC_DIR=$(CODE)/test/step2

STEP2_SRC=$(wildcard $(STEP2_SRC_DIR)/*.c)
STEP2_EXE_LINUX=$(patsubst $(STEP2_SRC_DIR)/%.c,$(STEP2_BUILD_DIR)/%-linux,$(STEP2_SRC))

.PHONY: all
all: build-code $(STEP2_EXE_LINUX)

.PHONY: clean
clean:
	rm -f $(STEP2_EXE_LINUX)

.PHONY: build-code
build-code:
	@make -C $(CODE)/build

$(STEP2_BUILD_DIR):
	mkdir -p $(STEP2_BUILD_DIR)

$(STEP2_EXE_LINUX): $(STEP2_BUILD_DIR)/%-linux: $(STEP2_SRC_DIR)/%.c $(STEP2_SRC_DIR)/test_io_syscall.h | $(STEP2_BUILD_DIR)
	gcc -o $@ -DTEST_IO_LINUX $<
