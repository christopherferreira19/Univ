#include "test_io_syscall.h"

int main() {
    int integer = 1;
    do {
	    TestGetInt(&integer);
	    TestPutInt(integer);
	    TestPutChar('\n');
    } while (integer != 0);

    return 0;
}
