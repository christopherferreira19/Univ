// synch.cc 
//      Routines for synchronizing threads.  Three kinds of
//      synchronization routines are defined here: semaphores, locks 
//      and condition variables (the implementation of the last two
//      are left to the reader).
//
// Any implementation of a synchronization routine needs some
// primitive atomic operation.  We assume Nachos is running on
// a uniprocessor, and thus atomicity can be provided by
// turning off interrupts.  While interrupts are disabled, no
// context switch can occur, and thus the current thread is guaranteed
// to hold the CPU throughout, until interrupts are reenabled.
//
// Because some of these routines might be called with interrupts
// already disabled (Semaphore::V for one), instead of turning
// on interrupts at the end of the atomic operation, we always simply
// re-set the interrupt state back to its original value (whether
// that be disabled or enabled).
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "synch.h"
#include "system.h"

//----------------------------------------------------------------------
// Semaphore::Semaphore
//      Initialize a semaphore, so that it can be used for synchronization.
//
//      "debugName" is an arbitrary name, useful for debugging.
//      "initialValue" is the initial value of the semaphore.
//----------------------------------------------------------------------

Semaphore::Semaphore (const char *debugName, int initialValue)
{
    name = debugName;
    value = initialValue;
    queue = new List;
}

//----------------------------------------------------------------------
// Semaphore::Semaphore
//      De-allocate semaphore, when no longer needed.  Assume no one
//      is still waiting on the semaphore!
//----------------------------------------------------------------------

Semaphore::~Semaphore ()
{
    delete queue;
}

//----------------------------------------------------------------------
// Semaphore::P
//      Wait until semaphore value > 0, then decrement.  Checking the
//      value and decrementing must be done atomically, so we
//      need to disable interrupts before checking the value.
//
//      Note that Thread::Sleep assumes that interrupts are disabled
//      when it is called.
//----------------------------------------------------------------------

void
Semaphore::P ()
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);	// disable interrupts

    while (value == 0)
      {				// semaphore not available
	  queue->Append ((void *) currentThread);	// so go to sleep
	  currentThread->Sleep ();
      }
    value--;			// semaphore available, 
    // consume its value

    (void) interrupt->SetLevel (oldLevel);	// re-enable interrupts
}

//----------------------------------------------------------------------
// Semaphore::V
//      Increment semaphore value, waking up a waiter if necessary.
//      As with P(), this operation must be atomic, so we need to disable
//      interrupts.  Scheduler::ReadyToRun() assumes that threads
//      are disabled when it is called.
//----------------------------------------------------------------------

void
Semaphore::V ()
{
    Thread *thread;
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    thread = (Thread *) queue->Remove ();
    if (thread != NULL)		// make thread ready, consuming the V immediately
	scheduler->ReadyToRun (thread);
    value++;
    (void) interrupt->SetLevel (oldLevel);
}

Lock::Lock (const char *debugName)
{
    name = debugName;
    holder = NULL;
    queue = new List();
}

Lock::~Lock ()
{
    delete queue;
}

bool Lock::isHeldByCurrentThread ()
{
    return currentThread == holder;
}

int Lock::getHoldCount()
{
    ASSERT(isHeldByCurrentThread());
    return holdCount;
}

void
Lock::Acquire ()
{
     IntStatus oldLevel = interrupt->SetLevel (IntOff);

     if (currentThread == holder) {
        holdCount++;
     }
     else {
        while (holder != NULL) {
            queue->Append ((void *) currentThread);
	        currentThread->Sleep ();
        }

        holder = currentThread;
        holdCount = 1;
     }

    (void) interrupt->SetLevel (oldLevel);	// re-enable interrupts
}

void
Lock::Release ()
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(currentThread == holder);
    ASSERT(holdCount > 0);

    holdCount--;
    if (holdCount == 0) {
        holder = NULL;
        // Wake up another thread
        Thread *thread = (Thread *) queue->Remove ();
        if (thread != NULL) {
	        scheduler->ReadyToRun (thread);
        }
    }

    (void) interrupt->SetLevel (oldLevel);	// re-enable interrupts
}

Condition::Condition (const char *debugName)
{
    queue = new List();
}

Condition::~Condition ()
{
    delete queue;
}

void
Condition::Wait (Lock * conditionLock)
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(conditionLock->isHeldByCurrentThread());

    conditionLock->Release();
    queue->Append((void*) currentThread);
    currentThread->Sleep();
    conditionLock->Acquire();

    (void) interrupt->SetLevel (oldLevel);
}

void
Condition::Signal (Lock * conditionLock)
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(conditionLock->isHeldByCurrentThread());

	Thread* thread = (Thread*) queue->Remove();
	if (thread != NULL) {
		scheduler->ReadyToRun(thread);
	}

    (void) interrupt->SetLevel (oldLevel);
}

void
Condition::Broadcast (Lock * conditionLock)
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(conditionLock->isHeldByCurrentThread());

    while (!queue->IsEmpty()) {
        Thread* thread = (Thread*) queue->Remove();
        scheduler->ReadyToRun(thread);
    }

    (void) interrupt->SetLevel (oldLevel);
}

RWLock::RWLock(const char *name_arg) {
	name = name_arg;
	status = FREE;
	queue = new List();
	readHolding = 0;
	writeQueued = 0;
}

RWLock::~RWLock() {
	delete queue;
}

typedef struct {
	bool read;
	Thread *thread;
} RWLockQueueEntry;

void
RWLock::AcquireRead() {
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(status != FREE || writeQueued == 0);
    ASSERT(status != READ || readHolding > 0);

    if (status == WRITE || writeQueued > 0) {
	RWLockQueueEntry* entry = new RWLockQueueEntry();
	entry->read = true;
	entry->thread = currentThread;
	queue->Append ((void *) entry);

	currentThread->Sleep ();
    }
    else {
	status = READ;
    	readHolding++;
    }

    (void) interrupt->SetLevel (oldLevel);
}

void
RWLock::AcquireWrite() {
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(status != FREE || writeQueued == 0);
    ASSERT(status != READ || readHolding > 0);

    if (status == FREE) {
	status = WRITE;
    }
    else {
	RWLockQueueEntry* entry = new RWLockQueueEntry();
	entry->read = false;
	entry->thread = currentThread;
	queue->Append ((void *) entry);
	writeQueued++;

	currentThread->Sleep ();
    }

    (void) interrupt->SetLevel (oldLevel);
}

void
RWLock::Release() {
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    ASSERT(status != FREE || writeQueued == 0);
    ASSERT(status != READ || readHolding > 0);

    if (readHolding > 0) {
	readHolding--;
    }

    if (readHolding == 0) {
	if (queue->IsEmpty()) {
	    status = FREE;
	}
	else {
	    RWLockQueueEntry* entry = (RWLockQueueEntry*) queue->Remove();
	    scheduler->ReadyToRun(entry->thread);
	    if (entry->read) {
		delete entry;
		status = READ;
		readHolding++;

		while ((entry = (RWLockQueueEntry*) queue->Remove()) != NULL && entry->read) {
		    scheduler->ReadyToRun(entry->thread);
		    readHolding++;
		    delete entry;
		}

		if (entry != NULL) {
		    queue->Prepend(entry);
		}
	    }
	    else {
		delete entry;
		status = WRITE;
		writeQueued--;
	    }
	}
    }

    (void) interrupt->SetLevel (oldLevel);	// re-enable interrupts
}
