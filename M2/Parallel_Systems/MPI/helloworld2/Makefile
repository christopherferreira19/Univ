SRC=$(wildcard *.c)
EXE=$(patsubst %.c,%,$(SRC))
RUN=$(patsubst %.c,%.run,$(SRC))

default: $(EXE)

.PHONY: $(RUN)
run: $(RUN)

$(EXE): %: %.c
	mpicc -o $@ $<

$(RUN): %.run: %
	mpirun -np 4 $<
