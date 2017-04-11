// exception.cc 
//      Entry point into the Nachos kernel from user programs.
//      There are two kinds of things that can cause control to
//      transfer back to here from user code:
//
//      syscall -- The user code explicitly requests to call a procedure
//      in the Nachos kernel.  Right now, the only function we support is
//      "Halt".
//
//      exceptions -- The user code does something that the CPU can't handle.
//      For instance, accessing memory that doesn't exist, arithmetic errors,
//      etc.  
//
//      Interrupts (which can also cause control to transfer from user
//      code into the Nachos kernel) are handled elsewhere.
//
// For now, this only handles the Halt() system call.
// Everything else core dumps.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "system.h"
#include "syscall.h"
#include "system.h"
#include "virtualmemory.h"

#ifdef STEP3
extern void CheckUserOverFlow();
extern void do_UserThreadExit(int exitStatus);
extern ntid_t do_UserThreadCreate(int f, int arg, int ret);
extern int do_UserThreadJoin(ntid_t tid);
#endif

#ifdef STEP6
#include "rtfm.h"
#endif


//----------------------------------------------------------------------
// UpdatePC : Increments the Program Counter register in order to resume
// the user program immediately after the "syscall" instruction.
//----------------------------------------------------------------------
static void
UpdatePC ()
{
    int pc = machine->ReadRegister (PCReg);
    machine->WriteRegister (PrevPCReg, pc);
    pc = machine->ReadRegister (NextPCReg);
    machine->WriteRegister (PCReg, pc);
    pc += 4;
    machine->WriteRegister (NextPCReg, pc);
}

//----------------------------------------------------------------------
// ExceptionHandler
//      Entry point into the Nachos kernel.  Called when a user program
//      is executing, and either does a syscall, or generates an addressing
//      or arithmetic exception.
//
//      For system calls, the following is the calling convention:
//
//      system call code -- r2
//              arg1 -- r4
//              arg2 -- r5
//              arg3 -- r6
//              arg4 -- r7
//
//      The result of the system call, if any, must be put back into r2. 
//
// And don't forget to increment the pc before returning. (Or else you'll
// loop making the same system call forever!
//
//      "which" is the kind of exception.  The list of possible exceptions 
//      are in machine.h.
//----------------------------------------------------------------------

void do_SC_Halt() {
	DEBUG ('a', "Shutdown, initiated by user program.\n");
	interrupt->Halt ();
}

#ifdef STEP2
void do_SC_Exit() {
	int exitStatus = machine->ReadRegister (4);
	DEBUG ('a', "user process exited with status code %d.\n", exitStatus);
#ifdef STEP3
	do_UserThreadExit(exitStatus);
#else
	interrupt->Halt();
#endif
}

void do_SC_GetChar() {
	DEBUG ('a', "GetChar by user program.\n");
	machine->WriteRegister(2, synchConsole->SynchGetChar());
}

void do_SC_GetString() {
	DEBUG ('a', "GetString by user program.\n");
	int addr = machine->ReadRegister (4);
	int size = machine->ReadRegister (5);
	char str[size];
	synchConsole->SynchGetString(str, size);
	VirtualMemory::CopyStringToMachine(str, addr, size + 10);
}

void do_SC_PutChar() {
	DEBUG ('a', "PutChar by user program.\n");
	char ch = machine->ReadRegister (4);
	synchConsole->SynchPutChar(ch);
}

void do_SC_PutString() {
	DEBUG ('a', "PutString by user program.\n");
	int addr = machine->ReadRegister (4);
	char str[MAX_STRING_SIZE];
	VirtualMemory::CopyStringFromMachine(addr, str, MAX_STRING_SIZE);
	synchConsole->SynchPutString(str, true);
}

void do_SC_GetInt() {
	int value;
	DEBUG ('a', "GetInt by user program.\n");
	int virtAddr = machine->ReadRegister (4);
	synchConsole->SynchGetInt(&value);
	VirtualMemory::WriteMem(virtAddr, 4, value);
}

void do_SC_PutInt() {
	DEBUG ('a', "PutInt by user program.\n");
	int n = machine->ReadRegister (4);
	synchConsole->SynchPutInt(n);
}
#endif

#ifdef STEP3
void do_SC_UserThreadCreate() {
	DEBUG ('a', "User thread creation.\n");
	int f = machine->ReadRegister (4);
	int arg = machine->ReadRegister (5);
	int ret = machine->ReadRegister (6);;
	int res = do_UserThreadCreate(f, arg, ret);

	machine->WriteRegister(2, res);
}

void do_SC_UserThreadExit() {
	DEBUG ('a', "User thread exiting.\n");
	do_UserThreadExit(0);
}

void do_SC_UserThreadJoin() {
	ntid_t tid = machine->ReadRegister(4);
	DEBUG ('a', "Join thread : %d.\n", tid);
	int res = do_UserThreadJoin(tid);
	machine->WriteRegister(2, res);
}
void do_SC_UserThreadYield(){
	currentThread->Yield();
}

void do_SC_UserThreadTid() {
	UserThread* currentUserThread = (UserThread*) currentThread->getUserThread();
	machine->WriteRegister(2, currentUserThread->getTid());
}

void do_SC_LockInit() {
	int lockAddr = machine->ReadRegister(4);
	VirtualMemory::WriteMem(lockAddr, 4, (int) new Lock("UserLock"));
}

void do_SC_LockAcquire() {
	int lockAddr = machine->ReadRegister(4);
	Lock* lock;
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	lock->Acquire();
}

void do_SC_LockRelease() {
	int lockAddr = machine->ReadRegister(4);
	Lock* lock;
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	lock->Release();
}

void do_SC_LockDestroy() {
	int lockAddr = machine->ReadRegister(4);
	Lock* lock;
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	delete lock;
}

void do_SC_SemaphoreInit() {
	int semAddr = machine->ReadRegister(4);
	int initialValue = machine->ReadRegister(5);
	int sem = (int) new Semaphore("UserSemaphore", initialValue);
	VirtualMemory::WriteMem(semAddr, 4, sem);
}

void do_SC_SemaphoreP() {
	int semAddr = machine->ReadRegister(4);
	Semaphore* sem;
	VirtualMemory::ReadMem(semAddr, 4, (int*) &sem);
	sem->P();
}

void do_SC_SemaphoreV() {
	int semAddr = machine->ReadRegister(4);
	Semaphore* sem;
	VirtualMemory::ReadMem(semAddr, 4, (int*) &sem);
	sem->V();
}

void do_SC_SemaphoreDestroy() {
	int semAddr = machine->ReadRegister(4);
	Semaphore* sem;
	VirtualMemory::ReadMem(semAddr, 4, (int*) &sem);
	delete sem;
}

void do_SC_CondInit() {
	int condAddr = machine->ReadRegister(4);
	VirtualMemory::WriteMem(condAddr, 4, (int) new Condition("UserCondition"));
}

void do_SC_CondWait() {
	int condAddr = machine->ReadRegister(4);
	int lockAddr = machine->ReadRegister(5);
	Condition* cond;
	Lock* lock;
	VirtualMemory::ReadMem(condAddr, 4, (int*) &cond);
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	cond->Wait(lock);
}

void do_SC_CondSignal() {
	int condAddr = machine->ReadRegister(4);
	int lockAddr = machine->ReadRegister(5);
	Condition* cond;
	Lock* lock;
	VirtualMemory::ReadMem(condAddr, 4, (int*) &cond);
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	cond->Signal(lock);
}

void do_SC_CondBroadcast() {
	int condAddr = machine->ReadRegister(4);
	int lockAddr = machine->ReadRegister(5);
	Condition* cond;
	Lock* lock;
	VirtualMemory::ReadMem(condAddr, 4, (int*) &cond);
	VirtualMemory::ReadMem(lockAddr, 4, (int*) &lock);
	cond->Broadcast(lock);
}

void do_SC_CondDestroy() {
	int condAddr = machine->ReadRegister(4);
	Condition* cond;
	VirtualMemory::ReadMem(condAddr, 4, (int*) &cond);
	delete cond;
}

void do_SC_RWLockInit() {
	int rwLockAddr = machine->ReadRegister(4);
	int rwLock = (int) new RWLock("UserRWLock");
	VirtualMemory::WriteMem(rwLockAddr, 4, rwLock);
}

void do_SC_RWLockAcquireRead() {
	int rwLockAddr = machine->ReadRegister(4);
	RWLock* rwLock;
	VirtualMemory::ReadMem(rwLockAddr, 4, (int*) &rwLock);
	rwLock->AcquireRead();
}

void do_SC_RWLockAcquireWrite() {
	int rwLockAddr = machine->ReadRegister(4);
	RWLock* rwLock;
	VirtualMemory::ReadMem(rwLockAddr, 4, (int*) &rwLock);
	rwLock->AcquireWrite();
}

void do_SC_RWLockRelease() {
	int rwLockAddr = machine->ReadRegister(4);
	RWLock* rwLock;
	VirtualMemory::ReadMem(rwLockAddr, 4, (int*) &rwLock);
	rwLock->Release();
}

void do_SC_RWLockDestroy() {
	int rwLockAddr = machine->ReadRegister(4);
	RWLock* rwLock;
	VirtualMemory::ReadMem(rwLockAddr, 4, (int*) &rwLock);
	delete rwLock;
}

#endif

#ifdef STEP4

void do_SC_ForkExec()
{
	int machine_filename = machine->ReadRegister(4);
	char kernel_filename[MAX_STRING_SIZE];
	VirtualMemory::CopyStringFromMachine(machine_filename, kernel_filename, MAX_STRING_SIZE);
	DEBUG ('a', "User ForkExcec, %s.\n", kernel_filename);
	npid_t npid = do_ForkExec(kernel_filename);
	machine->WriteRegister(2, npid);
}

void do_SC_WaitProcess()
{
	npid_t npid = machine->ReadRegister(4);
	int exitStatus = machine->ReadRegister(5);
	do_WaitProcess(npid, exitStatus);
}

void do_SC_GetNpid()
{
	machine->WriteRegister(2, currentProcess()->getNpid());
}

#endif

#ifdef STEP5

void do_SC_CreateDirectory()
{
	DEBUG ('a', "Create a new directory.\n");
	int addr = machine->ReadRegister (4);
	char str[MAX_STRING_SIZE];
	VirtualMemory::CopyStringFromMachine(addr, str, MAX_STRING_SIZE);
	int created = fileSystem->CreateDirectory(str);
	machine->WriteRegister(2, created);
}

void do_SC_RemoveDirectory()
{
	DEBUG ('a', "Create a new directory.\n");
	int addr = machine->ReadRegister (4);
	char str[MAX_STRING_SIZE];
	VirtualMemory::CopyStringFromMachine(addr, str, MAX_STRING_SIZE);
	int created = fileSystem->RemoveDirectory(str);
	machine->WriteRegister(2, created);
}

void do_SC_ChangeDirectory()
{
	DEBUG ('a', "Change directory.\n");
	int addr = machine->ReadRegister (4);
	char str[MAX_STRING_SIZE];
	VirtualMemory::CopyStringFromMachine(addr, str, MAX_STRING_SIZE);
	int changed = fileSystem->ChangeDirectory(str);
	machine->WriteRegister(2, changed);
}

void do_SC_PrintDirectory()
{
	DEBUG ('a', "Print current directory.\n");
	fileSystem->PrintCurrentDirectory();

}

void do_SC_GetCurrentDirectory()
{
	char path[MAX_STRING_SIZE];
	bool result = fileSystem->getCurrentDirectoryPath(path);
	int addr = machine->ReadRegister (4);
	VirtualMemory::CopyStringToMachine(path, addr, MAX_STRING_SIZE);
	machine->WriteRegister(2, result);
}


void do_SC_FileCreate() {
    char name[MAX_STRING_SIZE];
    int nameAddr = machine->ReadRegister (4);
    int size = machine->ReadRegister (5);
    VirtualMemory::CopyStringFromMachine(nameAddr, name, MAX_STRING_SIZE);
    int result = fileSystem->Create(name, size);
    machine->WriteRegister(2, result);
}

void do_SC_FileOpen() {
    char name[MAX_STRING_SIZE];
    int nameAddr = machine->ReadRegister (4);
    FileOpenMode mode = (FileOpenMode) machine->ReadRegister (5);
    VirtualMemory::CopyStringFromMachine(nameAddr, name, MAX_STRING_SIZE);
    nfid_t nfid = fileSystem->getOpenFiles()->Open(name, mode);
    machine->WriteRegister(2, nfid);
}

void do_SC_FileSeek() {
    nfid_t nfid = (nfid_t) machine->ReadRegister (4);
    int position = machine->ReadRegister (5);
    int result = fileSystem->getOpenFiles()->Seek(nfid, position);
    machine->WriteRegister(2, result);
}

void do_SC_FileRead() {
    char buffer[MAX_STRING_SIZE];
    nfid_t nfid = (nfid_t) machine->ReadRegister (4);
    int bufferAddr = machine->ReadRegister (5);
    int size = machine->ReadRegister (6);
    int result = fileSystem->getOpenFiles()->Read(nfid, buffer, size);
    VirtualMemory::CopyStringToMachine(buffer, bufferAddr, MAX_STRING_SIZE);
    machine->WriteRegister(2, result);
}

void do_SC_FileWrite() {
    char buffer[MAX_STRING_SIZE];
    nfid_t nfid = (nfid_t) machine->ReadRegister (4);
    int bufferAddr = machine->ReadRegister (5);
    int size = machine->ReadRegister (6);
    VirtualMemory::CopyStringFromMachine(bufferAddr, buffer, MAX_STRING_SIZE);
    int result = fileSystem->getOpenFiles()->Write(nfid, buffer, size);
    machine->WriteRegister(2, result);

}

void do_SC_FileClose() {
    nfid_t nfid = (nfid_t) machine->ReadRegister (4);
    fileSystem->getOpenFiles()->Close(nfid);
}

#endif


#ifdef STEP6
void do_SC_ReliableTranfertCreate()
{
    int rtfmAddr = machine->ReadRegister(4);
    int addrTo = machine->ReadRegister(5);
    int result = (int) new Rtfm(addrTo);
    VirtualMemory::WriteMem(rtfmAddr, 4, result);
}

void do_SC_ReliableTranfertDestroy()
{
    int addr = machine->ReadRegister(4);
    Rtfm *rtfm;
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    delete rtfm;
}

void do_SC_WaitForConnection()
{
    int addr = machine->ReadRegister(4);
    Rtfm *rtfm;
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    int result = rtfm->WaitForConnection();
    machine->WriteRegister(2, (int)result);    
}

void do_SC_Connect()
{
    int addr = machine->ReadRegister(4);
    Rtfm *rtfm;
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    int result = rtfm->Connect();
    machine->WriteRegister(2, (int)result);
}

void do_SC_Send()
{
    int addr = machine->ReadRegister(4);
    int dataAddr = machine->ReadRegister(5);
    Rtfm *rtfm;
    char str[MAX_STRING_SIZE];
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    VirtualMemory::CopyStringFromMachine(dataAddr, str, MAX_STRING_SIZE);
    int result = rtfm->Send(str);
    machine->WriteRegister(2, (int)result);
}

void do_SC_Receive()
{
    int addr = machine->ReadRegister(4);
    int dataAddr = machine->ReadRegister(5);
    Rtfm *rtfm;
    char str[MaxRtfmPktSize];
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    rtfm->Receive(str);
    VirtualMemory::CopyStringToMachine(str, dataAddr, MaxRtfmPktSize);
}

void do_SC_Close()
{
    int addr = machine->ReadRegister(4);
    Rtfm *rtfm;
    VirtualMemory::ReadMem(addr, 4, (int*) &rtfm);
    rtfm->Close();
}
#endif

void
ExceptionHandler (ExceptionType which)
{
    int type = machine->ReadRegister (2);
    
    if ((which == SyscallException)) {
		switch (type) {
			case SC_Halt: do_SC_Halt(); break;
#ifdef STEP2
			case SC_Exit: do_SC_Exit(); break;

			case SC_GetChar: do_SC_GetChar(); break;
			case SC_GetString: do_SC_GetString(); break;
			case SC_PutChar: do_SC_PutChar(); break;
			case SC_PutString: do_SC_PutString(); break;
			case SC_GetInt: do_SC_GetInt(); break;
			case SC_PutInt: do_SC_PutInt(); break;
#endif
#ifdef STEP3
			case SC_UserThreadCreate: do_SC_UserThreadCreate(); break;
			case SC_UserThreadExit: do_SC_UserThreadExit(); break;
			case SC_UserThreadJoin: do_SC_UserThreadJoin(); break;
			case SC_UserThreadYield: do_SC_UserThreadYield(); break;
			case SC_UserThreadTid: do_SC_UserThreadTid(); break;

			case SC_LockInit: do_SC_LockInit(); break;
			case SC_LockAcquire: do_SC_LockAcquire(); break;
			case SC_LockRelease: do_SC_LockRelease(); break;
			case SC_LockDestroy: do_SC_LockDestroy(); break;

			case SC_RWLockInit: do_SC_RWLockInit(); break;
			case SC_RWLockAcquireRead: do_SC_RWLockAcquireRead(); break;
		        case SC_RWLockAcquireWrite: do_SC_RWLockAcquireWrite(); break;
			case SC_RWLockRelease: do_SC_RWLockRelease(); break;
			case SC_RWLockDestroy: do_SC_RWLockDestroy(); break;

		        case SC_SemaphoreInit: do_SC_SemaphoreInit(); break;
			case SC_SemaphoreP: do_SC_SemaphoreP(); break;
			case SC_SemaphoreV: do_SC_SemaphoreV(); break;
			case SC_SemaphoreDestroy: do_SC_SemaphoreDestroy(); break;

			case SC_CondInit: do_SC_CondInit(); break;
			case SC_CondWait: do_SC_CondWait(); break;
			case SC_CondSignal: do_SC_CondSignal(); break;
			case SC_CondBroadcast: do_SC_CondBroadcast(); break;
			case SC_CondDestroy: do_SC_CondDestroy(); break;
#endif

#ifdef STEP4
			case SC_ForkExec: do_SC_ForkExec(); break;
			case SC_WaitProcess: do_SC_WaitProcess(); break;
			case SC_GetNpid: do_SC_GetNpid(); break;
#endif

#ifdef STEP5
			case SC_CreateDirectory: do_SC_CreateDirectory(); break;
			case SC_RemoveDirectory: do_SC_RemoveDirectory(); break;
			case SC_ChangeDirectory: do_SC_ChangeDirectory(); break;
			case SC_PrintDirectory: do_SC_PrintDirectory(); break;
			case SC_GetCurrentDirectory: do_SC_GetCurrentDirectory(); break;

			case SC_FileCreate: do_SC_FileCreate(); break;
			case SC_FileOpen: do_SC_FileOpen(); break;
                        case SC_FileSeek: do_SC_FileSeek(); break;
			case SC_FileRead: do_SC_FileRead(); break;
			case SC_FileWrite: do_SC_FileWrite(); break;
			case SC_FileClose: do_SC_FileClose(); break;
#endif
#ifdef STEP6
		        case SC_ReliableTranfertCreate: do_SC_ReliableTranfertCreate(); break;
		        case SC_ReliableTranfertDestroy: do_SC_ReliableTranfertDestroy(); break;
		        case SC_WaitForConnection: do_SC_WaitForConnection(); break;
		        case SC_Connect: do_SC_Connect(); break;
		        case SC_Send: do_SC_Send(); break;
		        case SC_Receive: do_SC_Receive(); break;
		        case SC_Close: do_SC_Close(); break;
#endif
			default:
				printf ("Unknown syscall %d\n", type);
				ASSERT (FALSE);
		}
    }
    else {
		switch (which) {
			case PageFaultException:
			{
			    int addr = machine->ReadRegister(BadVAddrReg);
#ifdef STEP4
			    AddrSpace *space = currentThread->space;

			    if(space->IsInStackFence(addr))
			    {
				printf ("User exception: stack overflow on [%d]0x%x, thread %s\n",
					VirtualMemory::VPN(addr), addr, currentThread->getName());
				ASSERT(FALSE);
			    }				
			    else if (VirtualMemory::TryAllocatePage(addr))
			    {
				return;
			    }
#endif
			    printf ("User exception: Page fault on 0x%x\n", addr);
			    ASSERT(FALSE);
			}
			case ReadOnlyException:
			{
			    printf ("User exception: Read only on 0x%x\n", machine->ReadRegister(BadVAddrReg));
			    ASSERT(FALSE);
			}
			case BusErrorException:
				printf ("User exception: Bus error on 0x%x\n", machine->ReadRegister(BadVAddrReg));
				ASSERT(FALSE);
			case AddressErrorException:
				printf ("User exception: Address error on 0x%x\n", machine->ReadRegister(BadVAddrReg));
				ASSERT(FALSE);
			case OverflowException:
				printf ("User exception: Overflow on 0x%x\n", machine->ReadRegister(BadVAddrReg));
				ASSERT(FALSE);
			case IllegalInstrException:
				printf ("User exception: Illegal instruction\n");
				ASSERT(FALSE);
			default:
				printf ("User exception: Unexpected %d %d\n", which, type);
				ASSERT (FALSE);
		}
    }
    
    // LB: Do not forget to increment the pc before returning!
    UpdatePC ();
    // End of addition
}
