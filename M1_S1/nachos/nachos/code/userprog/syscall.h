/* syscalls.h 
 * 	Nachos system call interface.  These are Nachos kernel operations
 * 	that can be invoked from user programs, by trapping to the kernel
 *	via the "syscall" instruction.
 *
 *	This file is included by user programs and by the Nachos kernel. 
 *
 * Copyright (c) 1992-1993 The Regents of the University of California.
 * All rights reserved.  See copyright.h for copyright notice and limitation 
 * of liability and disclaimer of warranty provisions.
 */

#ifndef SYSCALLS_H
#define SYSCALLS_H

#include "copyright.h"

/* system call codes -- used by the stubs to tell the kernel which system call
 * is being asked for
 */
 // Standard syscalls
#define SC_Halt			0
#define SC_Exit			1

// Console syscalls
#define SC_GetChar		200
#define SC_GetString  		201
#define SC_PutChar		203
#define SC_PutString  		204
#define SC_GetInt		205
#define SC_PutInt		206

// Thread syscalls
#define SC_UserThreadCreate     301
#define SC_UserThreadExit       302
#define SC_UserThreadJoin       303
#define SC_UserThreadYield      304
#define SC_UserThreadTid        305

// Synchronization syscalls
#define SC_LockInit             320
#define SC_LockAcquire          321
#define SC_LockRelease          322
#define SC_LockDestroy          323

#define SC_SemaphoreInit        330
#define SC_SemaphoreP           331
#define SC_SemaphoreV           332
#define SC_SemaphoreDestroy     333

#define SC_CondInit        	340
#define SC_CondWait        	341
#define SC_CondSignal      	342
#define SC_CondBroadcast   	343
#define SC_CondDestroy     	344

#define SC_RWLockInit         350
#define SC_RWLockAcquireRead  351
#define SC_RWLockAcquireWrite 352
#define SC_RWLockRelease      353
#define SC_RWLockDestroy      354

// Process syscalls
#define SC_ForkExec	  	400
#define SC_WaitProcess 	  	401
#define SC_GetNpid 	  	402

// Filesys syscalls
#define SC_CreateDirectory	505
#define SC_RemoveDirectory	506
#define SC_ChangeDirectory	507
#define SC_PrintDirectory	508
#define SC_GetCurrentDirectory	509

#define SC_FileCreate     520
#define SC_FileSeek       521
#define SC_FileOpen       522
#define SC_FileRead       523
#define SC_FileWrite      524
#define SC_FileClose      525

// Network syscalls
#define SC_ReliableTranfertCreate  600
#define SC_ReliableTranfertDestroy 601
#define SC_WaitForConnection       602
#define SC_Connect                 603
#define SC_Send                    604
#define SC_Receive                 605 
#define SC_Close                   606



#ifdef IN_USER_MODE

#define NULL 0

// LB: This part is read only on compiling the test/*.c files.
// It is *not* read on compiling test/start.S


/** General API */

/* Stop Nachos, and print out performance stats */
void Halt () __attribute__((noreturn));


/** Process API */

/* This user program is done (status = 0 means exited normally). */
void Exit (int status) __attribute__((noreturn));

/* A unique identifier for an executing user program (address space) */
typedef int npid_t;

#define FORK_EXEC_ERROR -1
/* Create a new process running the code inside the executable "name"
   And return its "npid" or FORK_EXEC_ERROR in case of an error */
npid_t ForkExec(char *name);

/* Only return once the the user process "npid" has finished.  
 * Return the exit status.
 */
void WaitProcess(npid_t npid, int* exitStatus);


/** Console API */

/* Get a character from stdin, synchronously */
char GetChar();

/* Get a string from stdin */
void GetString(char *s, int n);

/* Put a character to stdout, synchronously */
void PutChar(char c);

/* Put a string */
void PutString(const char s[]);

void PutInt(int n);

void GetInt(int *n);


/** Thread API */

/* Unique id for a running thread */
typedef int ntid_t;

/* User threads creation */
ntid_t UserThreadCreate(void f(void* arg), void* arg);

/* User threads destuction */
void UserThreadExit();

/* Wait for user thread tid */
int UserThreadJoin(ntid_t tid);

/* Yield current thread 
   i.e: give a chance to run to another thread
 */
int UserThreadYield();

/* Return the calling thread tid  */
ntid_t UserThreadTid();

/** Synchronization API */

typedef int mutex_t;

void LockInit(mutex_t* mutex);

void LockAcquire(mutex_t* mutex);

void LockRelease(mutex_t* mutex);

void LockDestroy(mutex_t* mutex);

typedef int sem_t;

void SemaphoreInit(sem_t* semaphore, int initialValue);

void SemaphoreP(sem_t* semaphore);
#define SemaphoreWait SemaphoreP

void SemaphoreV(sem_t* semaphore);
#define SemaphoreSignal SemaphoreV

void SemaphoreDestroy(sem_t* semaphore);

typedef int cond_t;

void CondInit(cond_t* cond);

void CondWait(cond_t* cond, mutex_t* lock);

void CondSignal(cond_t* cond, mutex_t* lock);

void CondBroadcast(cond_t* cond, mutex_t* lock);

void CondDestroy(cond_t* cond);


typedef int rwlock_t;

void RWLockInit(rwlock_t* rwlock);

void RWLockAcquireRead(rwlock_t* rwlock);

void RWLockAcquireWrite(rwlock_t* rwlock);

void RWLockRelease(rwlock_t* rwlock);

void RWLockDestroy(rwlock_t* rwlock);


/** Filesystem API */

int CreateDirectory(const char *name);

int RemoveDirectory(const char *name);

int ChangeDirectory(const char *name);

void PrintDirectory(const char *name);

/* Fill the argument "path" with the
   current directory path.

   Return 1 if successful or 0 otherwise
*/
int GetCurrentDirectory(char *path);


typedef int nfid_t;
#define NFID_ERROR -1

typedef enum {
	FILE_READ       = 1 << 0,
	FILE_WRITE      = 1 << 1,
	FILE_READ_WRITE = FILE_READ | FILE_WRITE,
} FileOpenMode;

/* Create a Nachos file, with "name" and size "size" */
int FileCreate(char *name, int size);

/* Open the Nachos file "name", and return an "OpenFileId" that can 
 * be used to read and write to the file.
 */
nfid_t FileOpen(char *name, FileOpenMode mode);

/* Change read/write cursor to "position" */
int FileSeek(nfid_t nfid, int position);

/* Write "size" bytes from "buffer" to the open file. */
void FileWrite(nfid_t nfid, char *buffer, int size);

/* Read "size" bytes from the open file into "buffer".  
 * Return the number of bytes actually read -- if the open file isn't
 * long enough, or if it is an I/O device, and there aren't enough 
 * characters to read, return whatever is available (for I/O devices, 
 * you should always wait until you can return at least one character).
 */
int FileRead(nfid_t nfid, char *buffer, int size);

/* Close the file, we're done reading and writing to it. */
void FileClose(nfid_t nfid);

// Reliable Transfer of Fixed-size Messages

typedef int net_t;

void ReliableTranfertCreate(void* rtfm, int addrTo);
void ReliableTranfertDestroy(void* rtfm);
int  WaitForConnection(void* rtfm);
int  Connect(void* rtfm);
int  Send(void* rtfm, char *data);
void Receive(void* rtfm, char *data);
void Close(void* rtfm);

#endif // IN_USER_MODE

#endif /* SYSCALL_H */
