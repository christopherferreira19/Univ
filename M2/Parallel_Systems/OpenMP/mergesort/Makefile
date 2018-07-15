SRC=$(wildcard *.c)
EXE=$(patsubst %.c,%,$(SRC))
RUN=$(patsubst %.c,%.run,$(SRC))

default: $(EXE)

.PHONY: $(RUN)
run: $(RUN)

$(EXE): %: %.c
	gcc -fopenmp -o $@ $<

$(RUN): %.run: %
	./$<
