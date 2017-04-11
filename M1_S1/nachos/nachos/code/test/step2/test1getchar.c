 #include "test_io_syscall.h"

 int main() {
    char ch;
    int i;

 	for (i=0; i<100; i++) {
 		ch = TestGetChar();
 		TestPutChar(ch);
 	}

    TestPutChar('\n');
 	return 0;
 }

