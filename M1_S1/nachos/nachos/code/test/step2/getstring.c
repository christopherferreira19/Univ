#include "test_io_syscall.h"

int main() {
    char str[5];
    TestGetString(str, 5);
    TestPutString(str);
    return 0;
}
