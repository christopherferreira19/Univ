#include "process.h"
#include "addrspace.h"
#include "system.h"

#ifdef STEP3
void StartProcess(int arg)
{
    Process *process = currentProcess();
    DEBUG ('t', "Starting process %d with UserThread : %s\n", process->getNpid(), currentThread->getName());
    process->Run();
}
#endif

Process::Process(npid_t npid_arg, char *filename)
{
  npid = npid_arg;
  OpenFile *executable = fileSystem->Open (filename);
  if (executable == NULL)
  {
      printf ("Unable to open file %s\n", filename);
      return;
  }

  space = new AddrSpace (executable, FALSE);
  delete executable;
  if (space == NULL) {
      printf ("Unable to initialize address space\n");
      return;
  }

#ifdef STEP3
  maxUserThread = space->getUserStackSize() / (PAGE_PER_THREAD * PageSize) - 1;
  ASSERT(maxUserThread > 0);  
  userThreadManager = new UserThreadManager(npid, space, maxUserThread);
#endif

#ifdef STEP4
  awaiting = new List();
#endif
  
#ifdef STEP5
  currentDirectorySector = fileSystem->getRootDirectorySector();
#endif
}

Process::~Process()
{
#ifdef STEP3
    delete userThreadManager;
#endif
#ifdef STEP4
    delete awaiting;
#endif
}

npid_t Process::getNpid() {
    return npid;
}

AddrSpace* Process::getAddrSpace()
{
    return space;
}

#ifdef STEP3
void Process::Schedule()
{
  // Create a main thread for the process ...
  UserThread* processMainThread = userThreadManager->CreateUserThread();
  // ...  and schedule it
  processMainThread->getSystemThread()->Fork(StartProcess, 0);
}
#endif

void Process::Run()
{
    currentThread->space = space;
    currentThread->space->InitRegisters();
    currentThread->space->RestoreState ();
    machine->WriteRegister(PCReg, 0);
    machine->WriteRegister(NextPCReg, 0 + 4);

    machine->Run ();		// jump to the user progam
    ASSERT (FALSE);		// machine->Run never returns;
}


#ifdef STEP3

UserThreadManager* Process::getUserThreadManager()
{
    return userThreadManager;
}
#endif

#ifdef STEP4
npid_t do_ForkExec(char* filename)
{
    Process *process = processManager->CreateProcess(filename);
    process->Schedule();
    return process == NULL
            ? -1
            : process->getNpid();
}

void do_WaitProcess(npid_t npid, int exitStatusAddr)
{
    int exitStatus;
    Process *process = processManager->getProcess(npid);
    process->AddAwaiting(currentThread, &exitStatus);
    IntStatus oldLevel = interrupt->SetLevel (IntOff);

    DEBUG('t', "### Process %d waiting on process %d", currentProcess()->getNpid(), npid);

    currentThread->Sleep();
    VirtualMemory::WriteMem(exitStatusAddr, 4, exitStatus);
    (void) interrupt->SetLevel (oldLevel);
}

typedef struct {
    Thread* thread;
    int *exitStatusAddr;
}  AwaitingEntry;

void Process::AddAwaiting(Thread* thread, int *exitStatusAddr)
{
    AwaitingEntry *entry = new AwaitingEntry();
    ASSERT(thread != NULL);
    ASSERT(awaiting != NULL);
    entry->thread = thread;
    entry->exitStatusAddr = exitStatusAddr;
    awaiting->Append(entry);
}

void Process::WakeAwaiting(int exitStatus)
{
    while (!awaiting->IsEmpty()) {
        AwaitingEntry* entry = (AwaitingEntry*) awaiting->Remove();
        *entry->exitStatusAddr = exitStatus;
        scheduler->ReadyToRun(entry->thread);
        delete entry;
    }
}
#endif

void Process::Finish(int exitStatus)
{
#ifdef STEP4
    WakeAwaiting(exitStatus);
#endif
}

#ifdef STEP5
int Process::getCurrentDirectorySector() {
    return currentDirectorySector;
}

void Process::setCurrentDirectorySector(int currentDirectorySector_arg) {
    currentDirectorySector = currentDirectorySector_arg;
}
#endif