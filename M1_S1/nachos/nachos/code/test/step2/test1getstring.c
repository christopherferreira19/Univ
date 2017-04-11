#include "test_io_syscall.h"

int main() {
 
    char str[20]; 
    TestGetString(str,20);
	TestPutString(str);
    TestPutString("");
 	return 0;
 }