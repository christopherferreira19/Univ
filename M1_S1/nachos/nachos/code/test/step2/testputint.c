#include "test_io_syscall.h"

void print() {
    
	int val;

    do{
    	val = 0;
    	TestGetInt(&val);
        TestPutInt(val);
        TestPutChar('\n');
    }while(val != 0);
}

int main() {
    print();
    return 0;
}