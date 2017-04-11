#ifndef SYNCHCONSOLE_H
#define SYNCHCONSOLE_H

#ifdef STEP2


#include "copyright.h"
#include "utility.h"
#include "console.h"
#include "synch.h"
#include <stdio.h>


class SynchConsole {
public:
    SynchConsole(char *readFile, char *writeFile);  // initialize the hardware console device
    ~SynchConsole(); // clean up console emulation
    void SynchPutChar(const char ch); // Unix putchar(3S)
    char SynchGetChar(); // Unix getchar(3S)
    void SynchPutString(const char *s, bool newline); // Unix puts(3S)
    void SynchGetString(char *s, int n); // Unix fgets(3S)
    void SynchPutInt(int n); // Unix sprintf
    void SynchGetInt(int *n); // Unix sscanf

private:
    Console *console;
    Lock* readLock;
    Lock* writeLock;
    Semaphore *readAvail;
    Semaphore *writeDone;

    static void ReadAvail(int arg);
    static void WriteDone(int arg);
    void UnlockedPutChar(const char ch); // Unix putchar(3S)
    char UnlockedGetChar(); // Unix getchar(3S)

};

void SynchConsoleTest(char *readFile, char *writeFile);

#else

// Dummy class when Step2 is not used 
class SynchConsole {};

#endif // STEP2
#endif // SYNCHCONSOLE_H
