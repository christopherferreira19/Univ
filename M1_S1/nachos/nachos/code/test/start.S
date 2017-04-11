/* Start.s 
 *	Assembly language assist for user programs running on top of Nachos.
 *
 *	Since we don't want to pull in the entire C library, we define
 *	what we need for a user program here, namely Start and the system
 *	calls.
 */

#include "syscall.h"

        .text   
        .align  2

/* -------------------------------------------------------------
 * __start
 *	Initialize running a C program, by calling "main". 
 *
 * 	NOTE: This has to be first, so that it gets loaded at location 0.
 *	The Nachos kernel always starts a program by jumping to location 0.
 * -------------------------------------------------------------
 */

	.globl __start
	.ent	__start
__start:
	jal	main
	move	$4,$2		
	jal	Exit	 /* if we return from main, exit(0) */
	.end __start


/* -------------------------------------------------------------
 * System call stubs:
 *	Assembly language assist to make system calls to the Nachos kernel.
 *	There is one stub per system call, that places the code for the
 *	system call into register r2, and leaves the arguments to the
 *	system call alone (in other words, arg1 is in r4, arg2 is 
 *	in r5, arg3 is in r6, arg4 is in r7)
 *
 * 	The return value is in r2. This follows the standard C calling
 * 	convention on the MIPS.
 * -------------------------------------------------------------
 */

#define SYSCALL_STUB(name) \
	.globl name; \
	.ent	name; \
name: \
	addiu $2,$0,SC_ ## name; \
	syscall; \
	j	$31; \
	.end name

# Standard syscalls
SYSCALL_STUB(Halt)
SYSCALL_STUB(Exit)

# Console syscalls
SYSCALL_STUB(GetChar)
SYSCALL_STUB(GetString)
SYSCALL_STUB(PutChar)
SYSCALL_STUB(PutString)
SYSCALL_STUB(GetInt)
SYSCALL_STUB(PutInt)

# Thread syscalls
SYSCALL_STUB(UserThreadExit)
	.globl UserThreadCreate
	.ent   UserThreadCreate
UserThreadCreate:
	addiu $6,$0,UserThreadExit
	addiu $2,$0,SC_UserThreadCreate
	syscall
	j	$31
	.end UserThreadCreate

SYSCALL_STUB(UserThreadJoin)
SYSCALL_STUB(UserThreadYield)
SYSCALL_STUB(UserThreadTid)

# Synchronization syscalls
SYSCALL_STUB(LockInit)
SYSCALL_STUB(LockAcquire)
SYSCALL_STUB(LockRelease)
SYSCALL_STUB(LockDestroy)

SYSCALL_STUB(SemaphoreInit)
SYSCALL_STUB(SemaphoreP)
SYSCALL_STUB(SemaphoreV)
SYSCALL_STUB(SemaphoreDestroy)
	
SYSCALL_STUB(CondInit)
SYSCALL_STUB(CondWait)
SYSCALL_STUB(CondSignal)
SYSCALL_STUB(CondBroadcast)
SYSCALL_STUB(CondDestroy)	
	
SYSCALL_STUB(RWLockInit)
SYSCALL_STUB(RWLockAcquireRead)
SYSCALL_STUB(RWLockAcquireWrite)
SYSCALL_STUB(RWLockRelease)
SYSCALL_STUB(RWLockDestroy)


# Process syscalls
SYSCALL_STUB(ForkExec)
SYSCALL_STUB(WaitProcess)
SYSCALL_STUB(GetNpid)

# Filesys syscalls
SYSCALL_STUB(CreateDirectory)
SYSCALL_STUB(RemoveDirectory)
SYSCALL_STUB(ChangeDirectory)
SYSCALL_STUB(PrintDirectory)
SYSCALL_STUB(GetCurrentDirectory)

SYSCALL_STUB(FileCreate)
SYSCALL_STUB(FileSeek)
SYSCALL_STUB(FileOpen)
SYSCALL_STUB(FileRead)
SYSCALL_STUB(FileWrite)
SYSCALL_STUB(FileClose)

# Newtwork syscalls
SYSCALL_STUB(ReliableTranfertCreate)
SYSCALL_STUB(ReliableTranfertDestroy)
SYSCALL_STUB(WaitForConnection)
SYSCALL_STUB(Connect)
SYSCALL_STUB(Send)
SYSCALL_STUB(Receive)
SYSCALL_STUB(Close)

	
/* dummy function to keep gcc happy */
        .globl  __main
        .ent    __main
__main:
        j       $31
        .end    __main
