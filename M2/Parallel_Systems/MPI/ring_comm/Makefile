N=1
SRC=$(wildcard *.c)
EXE=$(patsubst %.c,%,$(SRC))
RUN=$(patsubst %.c,%.run,$(SRC))

default: $(EXE)

.PHONY: $(RUN)
run: $(RUN)

$(EXE): %: %.c
	mpicc -o $@ $<

$(RUN): %.run: %
	@for i in $$(seq 1 $(N)); do \
	    echo $$i: mpirun -np 4 $<; \
	    mpirun -np 4 $<; \
	done
