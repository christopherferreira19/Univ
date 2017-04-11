#include "syscall.h"

int errorExitStatus = 1;
#define Try(code) if (!(code)) { return errorExitStatus; }; errorExitStatus++

int main() {
    char path[100];

    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(CreateDirectory("dir1"));
    Try(ChangeDirectory("dir1"));

    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(CreateDirectory("dir2"));
    Try(ChangeDirectory("dir2"));

    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(ChangeDirectory(".."));

    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(ChangeDirectory("."));

    Try(GetCurrentDirectory(path));
    PutString(path);

    return 0;
}