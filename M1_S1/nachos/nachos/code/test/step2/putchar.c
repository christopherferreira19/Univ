#include "test_io_syscall.h"

void print(char c, int n) {
    int i;
    for (i = 0; i < n; i++) {
        TestPutChar(c+i);
    }
    TestPutChar('\n');
}

int main() {
    print('a', 26);
    print('0', 10);
    
    return 0;
}
