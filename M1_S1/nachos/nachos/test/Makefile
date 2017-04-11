.PHONY: all
all: check

.PHONY: check
check:
	@make --no-print-directory -C step2
	@./test-run.sh

.PHONY: clean
clean:
	@make --no-print-directory -C step2 clean
	@rm -rf log/* step2/out step3/out step4/out