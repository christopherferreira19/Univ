SRC=$(wildcard *.c)
EXE=$(patsubst %.c,%,$(SRC))

all: $(EXE)

fgets_thread_test: fgets_thread_test.c
	gcc -o fgets_thread_test fgets_thread_test.c -lpthread

linux_stack_allocation: linux_stack_allocation.c
	gcc -o linux_stack_allocation linux_stack_allocation.c -lpthread

cwd_process_thread: cwd_process_thread.c
	gcc -o cwd_process_thread cwd_process_thread.c -lpthread
