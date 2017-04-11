#include "copyright.h"
#include "system.h"
#include "synchconsole.h"

void SynchConsole::ReadAvail(int arg) {
    SynchConsole* console = (SynchConsole*) arg;
    console->readAvail->V();
}
void SynchConsole::WriteDone(int arg) {
    SynchConsole* console = (SynchConsole*) arg;
    console->writeDone->V();
}

SynchConsole::SynchConsole(char *readFile, char *writeFile)
{
    readLock = new Lock("SynchConsole#readLock");
    writeLock = new Lock("SynchConsole#writeLock");
    readAvail = new Semaphore("read avail", 0);
    writeDone = new Semaphore("write done", 0);
    console = new Console(readFile, writeFile, ReadAvail, WriteDone, (int) this);
}

SynchConsole::~SynchConsole()
{
    delete console;
    delete writeDone;
    delete readAvail;
}

void SynchConsole::UnlockedPutChar(const char ch)
{
    console->PutChar(ch);
    writeDone->P();
}

void SynchConsole::SynchPutChar(const char ch)
{
    writeLock->Acquire();
    UnlockedPutChar(ch);
    writeLock->Release();
}

char SynchConsole::UnlockedGetChar()
{
    readAvail->P();
    return console->GetChar();
}

char SynchConsole::SynchGetChar()
{
    readLock->Acquire();
    char c = UnlockedGetChar();
    readLock->Release();
    return c;
}

void SynchConsole::SynchPutString(const char s[], bool newline)
{
    writeLock->Acquire();
    int it = 0;
    while(s[it] != '\0') {
	    UnlockedPutChar(s[it]);
	    it++;
    }
    if (newline == true)
        UnlockedPutChar('\n');
    writeLock->Release();
}

void SynchConsole::SynchGetString(char *s, int n)
{
    readLock->Acquire();
    int it;
    for (it=0; it < n-1; it++) {
	    s[it] = UnlockedGetChar();
        if(s[it] == '\n') {
            it++;
            break;
        }
        if(s[it] == EOF)
            break;
    }
    s[it] = '\0';

    readLock->Release();
}

void SynchConsole::SynchPutInt(int n)
{
        int size = 32;
        char s[size];

        snprintf(s, size, "%d", n);
        SynchPutString(s, false);
}

void SynchConsole::SynchGetInt(int *n)
{
        int size = 32;
        char s[32];

        SynchGetString(s, size);
        *n = 0;
        sscanf(s, "%d", n);
}
