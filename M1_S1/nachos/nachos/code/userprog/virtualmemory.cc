#include "virtualmemory.h"
#include "system.h"


int VirtualMemory::VPN(int virtualAddr) {
    return virtualAddr / PageSize;
}


#ifdef STEP4
static bool do_TryAllocatePage(unsigned int vpn) {
    ASSERT(machine->pageTable[vpn].physicalPage != STACK_FENCE);

    if (machine->pageTable[vpn].valid) {
        return true;
    }

    if (frameProvider->NumAvailFrame() == 0) {
        DEBUG('a', "No more available frame to allocate\n");
        return false;
    }

    machine->pageTable[vpn].physicalPage = frameProvider->GetEmptyFrame();
    machine->pageTable[vpn].valid = true;
    DEBUG('a', "Frame %d for page %d allocated\n", machine->pageTable[vpn].physicalPage, machine->pageTable[vpn].virtualPage);
    return true;
}

bool VirtualMemory::TryAllocatePage (int virtualAddr) {
    unsigned int vpn = VPN(virtualAddr);
    return do_TryAllocatePage(vpn);
}

bool VirtualMemory::TryAllocatePages(int virtualAddr, int size) {
    unsigned int vpnFrom = VPN(virtualAddr / PageSize);
    unsigned int vpnTo = VPN(virtualAddr + size - 1);
    for (unsigned int vpn = vpnFrom; vpn <= vpnTo; vpn++) {
        if (!do_TryAllocatePage(vpn)) {
            return false;
        }
    }

    return true;
}

static void SetReadOnlyPages(int virtualAddr, int size) {
    // Disabled for now, because we have no way to make sure the
    // code segment and data segment do not share a page.
    /*unsigned int vpnFrom = VPN(virtualAddr / PageSize);
    unsigned int vpnTo = VPN(virtualAddr + size - 1);
    for (unsigned int vpn = vpnFrom; vpn <= vpnTo; vpn++) {
        machine->pageTable[vpn].readOnly = true;
    }*/
}

#endif

#ifdef STEP4
bool VirtualMemory::ReadAt(OpenFile *file, int virtualaddr,
        int numBytes, int position, bool readOnly) {
    char* buf = new char[numBytes];
    file->ReadAt(buf, numBytes, position);

    bool ok = TryAllocatePages(virtualaddr, numBytes);
    if (ok) {
        for (int i = 0; ok && i < numBytes; i++) {
            ok = ok && machine->WriteMem(virtualaddr + i, 1, buf[i]);
        }
    }

    if (readOnly) {
        SetReadOnlyPages(virtualaddr, numBytes);
    }

    delete [] buf;

    return ok;
}
#else
bool VirtualMemory::ReadAt(OpenFile *file, int virtualaddr,
        int numBytes, int position, bool readOnly) {
	  return file->ReadAt (&(machine->mainMemory[virtualaddr]),
			      numBytes, position) == numBytes;
}
#endif

bool VirtualMemory::ReadMem(int addr, int size, int *value) {
#ifdef STEP4
    if (!TryAllocatePage(addr)) {
        return false;
    }
#endif
    return machine->ReadMem(addr, size, value);
}

bool VirtualMemory::WriteMem(int addr, int size, int value)  {
#ifdef STEP4
    if (!TryAllocatePage(addr)) {
        return false;
    }
#endif
    return machine->WriteMem(addr, size, value);
}

bool VirtualMemory::CopyStringFromMachine(int from, char *to, unsigned int size) {
    unsigned int i;
    for (i = 0; i < size - 1; i++) {
        ReadMem(from+i, 1, (int*)(to+i));
	    if (to[i] == '\0') {
    		return true;
    	}
    }

    to[i] = '\0';

    return true;
}

bool VirtualMemory::CopyStringToMachine(char* from, int to, unsigned int size) {
    unsigned int i;
    for (i = 0; i < size - 1; i++) {
    	WriteMem(to+i, 1, (int) from[i]);
        if (from[i] == '\0') {
		    return true;
	    }
    }

    machine->WriteMem(to+i, 1, '\0');
    return true;
}
