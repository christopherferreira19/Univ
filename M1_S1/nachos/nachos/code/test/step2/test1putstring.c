#include "test_io_syscall.h"

int main() {
	int i;
	char str[100]; 
	char ch; 

	for (i=0; i<99; i++)
	{
 		ch = TestGetChar();
		str[i] = ch;
	}
	str[i] = '\0';

	TestPutString(str);
	
	TestPutString("\n");
 	return 0;
    
	
}
