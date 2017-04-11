#include "syscall.h"


int main ()
{
    ForkExec("build/step2/putstring");

    char c;
    c = GetChar();
    PutChar(c);
    PutChar('\n');
    PutString("Main done");
}
