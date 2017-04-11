#include "test_io_syscall.h"

int main() {
    char ch;
    do {
	    ch = TestGetChar();
	    TestPutChar(ch);
    } while (ch != 'q');

    return 0;
}
