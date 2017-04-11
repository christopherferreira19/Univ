// addrspace.cc 
//      Routines to manage address spaces (executing user programs).
//
//      In order to run a user program, you must:
//
//      1. link with the -N -T 0 option 
//      2. run coff2noff to convert the object file to Nachos format
//              (Nachos object code format is essentially just a simpler
//              version of the UNIX executable object code format)
//      3. load the NOFF file into the Nachos file system
//              (if you haven't implemented the file system yet, you
//              don't need to do this last step)
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "system.h"
#include "addrspace.h"
#include "virtualmemory.h"
#include "noff.h"

#include <strings.h>		/* for bzero */

//----------------------------------------------------------------------
// SwapHeader
//      Do little endian to big endian conversion on the bytes in the 
//      object file header, in case the file was generated on a little
//      endian machine, and we're now running on a big endian machine.
//----------------------------------------------------------------------
    
static void
SwapHeader (NoffHeader * noffH)
{
    noffH->noffMagic = WordToHost (noffH->noffMagic);
    noffH->code.size = WordToHost (noffH->code.size);
    noffH->code.virtualAddr = WordToHost (noffH->code.virtualAddr);
    noffH->code.inFileAddr = WordToHost (noffH->code.inFileAddr);
    noffH->initData.size = WordToHost (noffH->initData.size);
    noffH->initData.virtualAddr = WordToHost (noffH->initData.virtualAddr);
    noffH->initData.inFileAddr = WordToHost (noffH->initData.inFileAddr);
    noffH->uninitData.size = WordToHost (noffH->uninitData.size);
    noffH->uninitData.virtualAddr =
	WordToHost (noffH->uninitData.virtualAddr);
    noffH->uninitData.inFileAddr = WordToHost (noffH->uninitData.inFileAddr);
}

//----------------------------------------------------------------------
// AddrSpace::AddrSpace
//      Create an address space to run a user program.
//      Load the program from a file "executable", and set everything
//      up so that we can start executing user instructions.
//
//      Assumes that the object code file is in NOFF format.
//
//      First, set up the translation from program memory to physical 
//      memory.  For now, this is really simple (1:1), since we are
//      only uniprogramming, and we have a single unsegmented page table
//
//      "executable" is the file containing the object code to load into memory
//----------------------------------------------------------------------

AddrSpace::AddrSpace (OpenFile * executable, bool initMemory)
{
    NoffHeader noffH;
    unsigned int i, size;

    executable->ReadAt ((char *) &noffH, sizeof (noffH), 0);
    if ((noffH.noffMagic != NOFFMAGIC) &&
	(WordToHost (noffH.noffMagic) == NOFFMAGIC))
	SwapHeader (&noffH);
    ASSERT (noffH.noffMagic == NOFFMAGIC);

// how big is address space?
#ifdef STEP4
    numPages = MAX_VIRTUAL_PAGES;
    size = PageSize * numPages;

    // Compute userStackSize, we want it to be a multiple of PageSize
    int codeDataSize = (noffH.code.size + noffH.initData.size + noffH.uninitData.size);
    if (codeDataSize % PageSize > 0) {
	codeDataSize += PageSize - (codeDataSize % PageSize);
    }
    userStackSize = size - codeDataSize;

    initialSP = size; 
#else
    userStackSize = UserStackSize;
    size = noffH.code.size + noffH.initData.size + noffH.uninitData.size + userStackSize;
    numPages = divRoundUp (size, PageSize);
    size = numPages * PageSize;

    initialSP = size;
    initialSP -= initialSP % PageSize;
    userStackSize -= userStackSize % PageSize;
#endif


#ifndef STEP4
    // check we're not trying
    // to run anything too big --
    // at least until we have
    // virtual memory
    ASSERT (numPages <= NumPhysPages);
#endif
    
    DEBUG ('a', "Initializing address space, num pages %d, size %d\n",
	   numPages, size);
// first, set up the translation 
    pageTable = new TranslationEntry[numPages];
    for (i = 0; i < numPages; i++)
      {
	  pageTable[i].virtualPage = i;
#ifdef STEP4
          pageTable[i].physicalPage = -1;
          pageTable[i].valid = FALSE;
#else
	  pageTable[i].physicalPage = i;
	  pageTable[i].valid = TRUE;
#endif
	  pageTable[i].use = FALSE;
	  pageTable[i].dirty = FALSE;
	  pageTable[i].readOnly = FALSE;
      }

// zero out the entire address space, to zero the unitialized data segment 
// and the stack segment
    if(initMemory == TRUE)
#ifdef STEP4
    bzero (machine->mainMemory, NumPhysPages * PageSize);
#else
    bzero (machine->mainMemory, size);
#endif

// Setup the machine page tables so that we can write code and data segments
    TranslationEntry* oldPageTable = machine->pageTable;
    int oldNumPages = machine->pageTableSize;
    machine->pageTable = pageTable;
    machine->pageTableSize = numPages;

// then, copy in the code and data segments into memory
    if (noffH.code.size > 0) {
        DEBUG ('a', "Initializing code segment, at 0x%x, size %d\n",
                noffH.code.virtualAddr, noffH.code.size);
        if (!VirtualMemory::ReadAt(executable, noffH.code.virtualAddr, noffH.code.size,
                noffH.code.inFileAddr, true)) {
            printf("Initialization of code segment, at 0x%x, size %d failed\n",
		            noffH.code.virtualAddr, noffH.code.size);
            ASSERT(false);
        }
    }
    if (noffH.initData.size > 0) {
        DEBUG ('a', "Initializing data segment, at 0x%x, size %d\n",
            noffH.initData.virtualAddr, noffH.initData.size);
        if (!VirtualMemory::ReadAt(executable, noffH.initData.virtualAddr, noffH.initData.size,
                noffH.initData.inFileAddr, false)) {
            printf("Initialization of data segment, at 0x%x, size %d failed\n",
                    noffH.initData.virtualAddr, noffH.initData.size);
            ASSERT(false);
        }
    }

// Now that the code & data segments are set up, restore the machine page tables
    pageTable = machine->pageTable;
    numPages = machine->pageTableSize;
    machine->pageTable = oldPageTable;
    machine->pageTableSize = oldNumPages;
}

//----------------------------------------------------------------------
// AddrSpace::~AddrSpace
//      Dealloate an address space.  Nothing for now!
//----------------------------------------------------------------------

AddrSpace::~AddrSpace ()
{
#ifdef STEP4
  for (unsigned int i = 0; i < numPages; i++) {
    frameProvider->ReleaseFrame(pageTable[i].physicalPage);
  }
#endif
  // LB: Missing [] for delete
  // delete pageTable;
  delete [] pageTable;
  // End of modification
}

//----------------------------------------------------------------------
// AddrSpace::InitRegisters
//      Set the initial values for the user-level register set.
//
//      We write these directly into the "machine" registers, so
//      that we can immediately jump to user code.  Note that these
//      will be saved/restored into the currentThread->userRegisters
//      when this thread is context switched out.
//----------------------------------------------------------------------

void
AddrSpace::InitRegisters ()
{
    int i;

    for (i = 0; i < NumTotalRegs; i++)
	machine->WriteRegister (i, 0);

    // Initial program counter -- must be location of "Start"
    machine->WriteRegister (PCReg, 0);

    // Need to also tell MIPS where next instruction is, because
    // of branch delay possibility
    machine->WriteRegister (NextPCReg, 4);

    // Set the stack register to the end of the address space, where we
    // allocated the stack; but subtract off a bit, to make sure we don't
    // accidentally reference off the end!
    machine->WriteRegister (StackReg, initialSP);
    DEBUG ('a', "Initializing stack register to 0x%x\n",
	   initialSP);
}

//----------------------------------------------------------------------
// AddrSpace::SaveState
//      On a context switch, save any machine state, specific
//      to this address space, that needs saving.
//
//      For now, nothing!
//----------------------------------------------------------------------

void
AddrSpace::SaveState ()
{
    pageTable = machine->pageTable;
    numPages = machine->pageTableSize;
}

//----------------------------------------------------------------------
// AddrSpace::RestoreState
//      On a context switch, restore the machine state so that
//      this address space can run.
//
//      For now, tell the machine where to find the page table.
//----------------------------------------------------------------------

void
AddrSpace::RestoreState ()
{
    machine->pageTable = pageTable;
    machine->pageTableSize = numPages;
}

unsigned int AddrSpace::getInitialSP ()
{
    return initialSP;
}

unsigned int AddrSpace::getUserStackSize ()
{
    return userStackSize;
}

#ifdef STEP4
bool AddrSpace::SetStackFence(int vpn)
{
    ASSERT(0 < vpn && vpn < MAX_VIRTUAL_PAGES);

    if(pageTable[vpn].use == TRUE)
    {
	printf("UserThread stack corruption\n");
	ASSERT(FALSE);
    }

    DEBUG ('a', "UserThread stack fence set, in %d\n", vpn);

    pageTable[vpn].physicalPage = STACK_FENCE;
    pageTable[vpn].readOnly = TRUE;
    return TRUE;
}
#endif

bool AddrSpace::IsInStackFence(int virtualAddr)
{
#ifdef STEP4
    int id = VirtualMemory::VPN(virtualAddr);
    if(machine->pageTable[id].physicalPage == STACK_FENCE)
    {
	return true;
    }
#endif
    
    return false;
}

void AddrSpace::ReleasePage(int virtualAddr, int pageNb)
{
    int vpn = VirtualMemory::VPN(virtualAddr);

    for(int i = 0; i < pageNb; i++)
    {
#ifdef STEP4
	ASSERT(vpn <= MAX_VIRTUAL_PAGES);
	if (pageTable[vpn].use == TRUE && pageTable[vpn].physicalPage != STACK_FENCE) {
		frameProvider->ReleaseFrame(pageTable[vpn].physicalPage);
	}
    	pageTable[vpn].virtualPage = vpn;
    	pageTable[vpn].physicalPage = -1;
  	pageTable[vpn].valid = FALSE;
#else
  	pageTable[vpn].physicalPage = vpn;
  	pageTable[vpn].valid = TRUE;
#endif
  	pageTable[vpn].use = FALSE;
  	pageTable[vpn].dirty = FALSE;
  	pageTable[vpn].readOnly = FALSE;

	vpn--;
   }
}
