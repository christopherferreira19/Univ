#include "test_io_syscall.h"

int main() {
	char c;
	while ((c = TestGetChar()) != 'q') {
		TestPutChar(c);
	}

	return 0;
}