#ifndef VIRTUAL_MEMORY_H
#define VIRTUAL_MEMORY_H

#include "filesys.h"

/**
    This class provides some static utility functions to manage the
    user memory while taking care of allocating pages when necessary
    --
    This class relies on the pageTable stored inside the machine to
    do all its translations.
 */
class VirtualMemory {
public:
    static int VPN(int virtualaddr);
    static bool TryAllocatePage(int virtualAddr);
    static bool TryAllocatePages(int virtualAddr, int size);
    static bool ReadMem(int addr, int size, int *value); // Equivalent to ReadMem except that it allocates the page if necessary
    static bool WriteMem(int addr, int size, int value); // Equivalent to WriteMem except that it allocates the page if necessary
    static bool ReadAt(OpenFile *executable, int virtualaddr,
        int numBytes, int position, bool readOnly); // Read numBytes from executable at position and stores them at virtualAddr
    static bool CopyStringFromMachine(int from, char *to, unsigned int size);
    static bool CopyStringToMachine(char* from, int to, unsigned int size);
    };

#endif
