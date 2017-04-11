#include "test_io_syscall.h"

void print() {
    
	int val;

    do{
    	TestGetInt(&val);
        TestPutChar('\n');
    }while(val != 0);
}

int main() {
    print();
    return 0;
}