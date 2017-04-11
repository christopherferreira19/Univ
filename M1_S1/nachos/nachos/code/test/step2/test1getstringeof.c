#include "test_io_syscall.h"

int main() {
	int i;
	
    char str[5];
    TestGetString(str, 5);
    TestPutString(str);
    for(i=0; i<5; i++)
    	{  
    		TestPutInt(str[i]);
    		if(str[i]=='\0')
    			break;

    	}
    return 0;
} 