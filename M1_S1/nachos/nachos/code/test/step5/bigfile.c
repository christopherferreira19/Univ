#include "syscall.h"

char text[] = "test text tezt";
char buf[sizeof(text)];

int main() {
    FileCreate("test", 12345);
    nfid_t nfid1 = FileOpen("test", FILE_WRITE);

    FileWrite(nfid1, text, sizeof(text));

    FileClose(nfid1);

    nfid_t nfid2 = FileOpen("test", FILE_READ);

    FileRead(nfid2, buf, sizeof(text));
    buf[sizeof(text)] = '\0';

    FileClose(nfid2);

    PutString("# Read #");
    PutString(buf);
    PutString("########");

    PutString("Done");
}