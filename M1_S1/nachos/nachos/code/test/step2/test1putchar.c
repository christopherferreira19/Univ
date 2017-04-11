 #include "test_io_syscall.h"

 int main() {
 	int i;
 	
 	for (i=97; i<127; i++)
 	{
 		TestPutChar(i);
 	}
 	TestPutChar('\n');
    
 	for (i=64; i<96; i++)
 	{
 		TestPutChar(i);
 	}
 	TestPutChar('\n');

 	for (i=32; i<64; i++)
 	{
 		TestPutChar(i);
 	}
 	TestPutChar('\n');
 	return 0;
 }

