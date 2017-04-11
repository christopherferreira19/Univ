#include "syscall.h"

int errorExitStatus = 1;
#define Try(code) if (!(code)) { return errorExitStatus; }; errorExitStatus++

int main() {
    char path[100];

    Try(CreateDirectory("dir1"));
    Try(ChangeDirectory("dir1"));
    Try(CreateDirectory("dir2"));
    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(CreateDirectory("/dir3"));
    Try(ChangeDirectory("../dir3"));
    Try(GetCurrentDirectory(path));
    PutString(path);

    Try(ChangeDirectory("../dir1/dir2"));
    Try(GetCurrentDirectory(path));
    PutString(path);
}