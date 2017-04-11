#ifdef STEP3

#include "system.h"
#include "error.h"
#include "userthreadmanager.h"
#include "userthread.h"

void StartUserThread(int arg)
{
    UserThreadArg *args = (UserThreadArg*)arg;
    int stackPosition = args->userThread->getStackBegin();

    ASSERT(stackPosition > 0);
    ASSERT(stackPosition % 4 == 0);
    
    machine->WriteRegister(StackReg, stackPosition);
    machine->WriteRegister(PCReg, args->f);
    machine->WriteRegister(NextPCReg, args->f + 4);
    machine->WriteRegister(4, args->arg);
    machine->WriteRegister(RetAddrReg, args->ret);

    DEBUG ('t', "Starting Thread %s at PC: 0x%x, stack: 0x%x\n", currentThread->getName(), args->f, stackPosition);

    delete args;

    machine->Run();
}

int do_UserThreadCreate(int f, int arg, int ret)
{
    IntStatus oldLevel = interrupt->SetLevel (IntOff);
    UserThreadManager *manager = currentProcess()->getUserThreadManager();
    UserThread *userThread = manager->CreateUserThread();

    if(userThread == NULL)
	return -1;

    UserThreadArg *args = new UserThreadArg();
    args->arg = arg;
    args->f = f;
    args->ret = ret;
    args->userThread = userThread;
    
    userThread->getSystemThread()->Fork(StartUserThread, (int)args);

    interrupt->SetLevel (oldLevel);

    return userThread->getTid();
}

void do_UserThreadExit(int exitStatus)
{
    DEBUG ('t', "Exiting Thread %s\n", currentThread->getName());
    UserThread* userThread = currentThread->getUserThread();
    UserThreadManager *manager = currentProcess()->getUserThreadManager();
    manager->DeleteThread(userThread->getTid(), exitStatus);
}

int do_UserThreadJoin(ntid_t tid)
{
    if(tid < 0)
	return ERWRONG_INPUT;

    UserThread *currentUserThread = (UserThread*)currentThread->getUserThread();
    
    if(currentUserThread->getTid() == tid)
	return ERWRONG_INPUT;

    UserThreadManager *manager = currentProcess()->getUserThreadManager();
    UserThread *userThread = manager->getUserThread(tid);

    IntStatus oldLevel = interrupt->SetLevel (IntOff);
    if(userThread != NULL)
    {
	userThread->AddWaitingForFinish(currentThread);
	currentThread->Sleep();
    }
    interrupt->SetLevel (oldLevel);

    return NOERROR;
}


UserThread::UserThread(char* name, npid_t npid_arg, AddrSpace *space, ntid_t tid_arg, int stackBegin_arg)
{
    npid = npid_arg;
    DEBUG ('t', "Creating Thread %s\n", name);
    tid = tid_arg;
    Thread *newThread = new Thread(name, this);
    sysThread = newThread;
    sysThread->space = space;

    stackBegin = stackBegin_arg - 4;

    waitingForFinish = new List();
}

UserThread::~UserThread ()
{
    delete waitingForFinish;
}

npid_t UserThread::getNpid() {
    return npid;
}

ntid_t UserThread::getTid()
{
    return tid;
}

const char* UserThread::getName()
{
    return sysThread->getName();
}

int UserThread::getStackBegin()
{
    return stackBegin;
}

Thread* UserThread::getSystemThread()
{
    return sysThread;
}

void UserThread::WakeWaitingForFinish()
{
    waitingForFinish->Mapcar(SchedulerReadyToRun);
}
    
void UserThread::AddWaitingForFinish(Thread *thread)
{
    DEBUG ('t', "Thread %s waiting thread %s\n", thread->getName (), sysThread->getName());
    waitingForFinish->Append(thread);
}

void UserThread::Finish() {
    interrupt->SetLevel (IntOff);
    WakeWaitingForFinish();
}

#endif
