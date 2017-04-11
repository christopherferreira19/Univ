SRC=$(wildcard *.c)
EXE=$(patsubst %.c,%,$(SRC))

all: $(EXE)

producer-consumer-gen: producer-consumer-gen.c
	gcc -o $@ $<
