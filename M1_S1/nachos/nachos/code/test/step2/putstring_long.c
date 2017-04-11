#include "test_io_syscall.h"

int main() {	
    int long_string = 512 * 2;
    char str[long_string];

    int i;
    for(i=0;i<long_string - 2;i++) {
	    str[i] = 'a' + (i % 26);
    }
    
    str[long_string - 1] = '\0';
    TestPutChar('\n');
    TestPutString(str);
    TestPutChar('\n');

    return 0;
}
