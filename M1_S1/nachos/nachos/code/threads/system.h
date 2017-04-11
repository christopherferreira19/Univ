// system.h 
//      All global variables used in Nachos are defined here.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#ifndef SYSTEM_H
#define SYSTEM_H

#include "copyright.h"
#include "utility.h"
#include "process.h"
#include "processmanager.h"
#include "thread.h"
#include "scheduler.h"
#include "interrupt.h"
#include "stats.h"
#include "timer.h"
#include "virtualmemory.h"


// Initialization and cleanup routines
extern void Initialize (int argc, char **argv);	// Initialization,
						// called before anything else
extern void Cleanup ();		// Cleanup, called when

extern Thread *currentThread;	// the thread holding the CPU
extern Thread *threadToBeDestroyed;	// the thread that just finished
extern Scheduler *scheduler;	// the ready list
extern Interrupt *interrupt;	// interrupt status
extern Statistics *stats;	// performance metrics
extern Timer *timer;		// the hardware alarm clock

#ifdef USER_PROGRAM
#include "machine.h"
extern Machine *machine;	// user program memory and registers
#endif

#ifdef FILESYS_NEEDED		// FILESYS or FILESYS_STUB
#include "filesys.h"
extern FileSystem *fileSystem;
#endif

#ifdef FILESYS
#include "synchdisk.h"
extern SynchDisk *synchDisk;
#endif

#ifdef NETWORK
#include "post.h"
extern PostOffice *postOffice;
#endif

#ifdef STEP2
#include "../userprog/synchconsole.h"
#define MAX_STRING_SIZE 512
extern SynchConsole *synchConsole;	// The console (synchronous)
#endif

#ifdef STEP3
extern ProcessManager *processManager;
extern Process *currentProcess();	// the process whose thread holds the CPU
#define PAGE_PER_THREAD 4
#endif

#ifdef STEP4
#include "frameprovider.h"
#include <climits>
extern FrameProvider *frameProvider;
#define STACK_FENCE UINT_MAX - 2
#define MAX_VIRTUAL_PAGES 1024
#endif

#ifdef STEP6
#include "timewait.h"
class TimeWait;
extern TimeWait *timeWait;
#endif


#endif // SYSTEM_H
