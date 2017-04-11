#ifndef __LINUX_IO__
#define __LINUX_IO__

#include "syscall.h"

#ifdef TEST_IO_LINUX
    #include "stdio.h"

    #define TestGetChar() (char) getchar()
    #define TestPutChar(c) (void) putchar(c)
    #define TestGetString(s, n) (void) fgets(s, n, stdin)
    #define TestPutString(s) (void) puts(s)
    char test_io_syscall_int_buf[32];
    #define TestGetInt(n) (fgets(test_io_syscall_int_buf, 32, stdin) ,\
                            (void) sscanf(test_io_syscall_int_buf, "%d", n))
    #define TestPutInt(n) (snprintf(test_io_syscall_int_buf, 32, "%d", n), \
                            (void) fputs(test_io_syscall_int_buf, stdout))
#else
    #define TestGetChar() GetChar()
    #define TestPutChar(c) PutChar(c)
    #define TestGetString(s, n) GetString(s, n)
    #define TestPutString(s) PutString(s)
    #define TestGetInt(n) GetInt(n)
    #define TestPutInt(n) PutInt(n)
#endif

#endif