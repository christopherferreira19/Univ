#ifdef STEP3

#include "userthreadmanager.h"

#define STACK_FENCEPOST 0xdeadbeef

UserThreadManager::UserThreadManager(npid_t npid_arg, AddrSpace *addrSpace, int maxThread)
{
    npid = npid_arg;
    space = addrSpace;
    stackOffset = PAGE_PER_THREAD * PageSize;
    
    maxUserThread = maxThread;
    threadArray = new UserThread*[maxUserThread];
    
    int i;
    for(i=0;i < maxUserThread;i++)
    {
	threadArray[i] = NULL;
    }
    threadCount = 0;
    nextTid = 0;
}

UserThread* UserThreadManager::CreateUserThread()
{
    int tid = getNewTid();
    if(tid == -1)
    {
	DEBUG ('t', "Could not create User Thread\n");
	return NULL;
    }

    int id = getIdfromTid(tid);
    char *name = new char[32];
    snprintf(name, 32, "User(npid: %d, tid: %d)", npid, tid);
    int stackBegin = space->getInitialSP() - stackOffset * id;
    UserThread* newUserThread = new UserThread(name, npid, space, tid, stackBegin);

    threadArray[id] = newUserThread;
    threadCount++;
#ifdef STEP4
    DEBUG('a', "Thread %s Stack:  [%d]0x%x - [%d]0x%x\n",
            name,
            VirtualMemory::VPN(stackBegin - 1),
	    stackBegin - 1,
            VirtualMemory::VPN(stackBegin - (PAGE_PER_THREAD-1) * PageSize),
	    stackBegin - (PAGE_PER_THREAD-1) * PageSize);
    DEBUG('a', "Thread %s Fence:  [%d]0x%x - [%d]0x%x\n",
            name,
            VirtualMemory::VPN(stackBegin - (PAGE_PER_THREAD-1) * PageSize - 1),
	    stackBegin - (PAGE_PER_THREAD-1) * PageSize - 1,
	    VirtualMemory::VPN(stackBegin - PAGE_PER_THREAD * PageSize),
	    stackBegin - PAGE_PER_THREAD * PageSize);
    space->SetStackFence(VirtualMemory::VPN(stackBegin - stackOffset));
#endif

    return newUserThread;
}


UserThreadManager::~UserThreadManager()
{
    delete[] threadArray;
}


ntid_t UserThreadManager::getNewTid()
{
    if(threadCount >= maxUserThread)
	return -1;
    
    UserThread *current = threadArray[nextTid % maxUserThread];
    while(current != NULL)
    {
	nextTid = nextTid + 1;
	current = threadArray[nextTid % maxUserThread];
    }
    return nextTid;
}

UserThread* UserThreadManager::getUserThread(ntid_t tid)
{
    UserThread* thread = threadArray[getIdfromTid(tid)];
    return thread != NULL && thread->getTid() == tid
            ? thread
            : NULL;
}

void UserThreadManager::DeleteThread(ntid_t tid, int exitStatus)
{
    int id = getIdfromTid(tid);    
    UserThread* toDelete = threadArray[id];
    Thread *sysThread = toDelete->getSystemThread();
    
    ASSERT(toDelete != NULL);
    ASSERT(toDelete->getTid() == tid);
#ifdef STEP4
    space->ReleasePage(toDelete->getStackBegin(), PAGE_PER_THREAD);    
#endif
    toDelete->Finish();
    delete toDelete;
    threadArray[id] = NULL;
    threadCount--;
    if(threadCount == 0)
    {
    	if(processManager->ExitProcess(npid, exitStatus) == 0)
	    interrupt->Halt();
    }
    sysThread->Finish();
}

void UserThreadManager::Print()
{
    printf("Thread Array:\n");
    
    for (int i=0; i < maxUserThread; i++)
    {
	if (threadArray[i] == NULL) break;
	printf("Thread(id: %d, name: %s)\n", threadArray[i]->getTid(), threadArray[i]->getName());
    }
}

int UserThreadManager::getIdfromTid(ntid_t tid)
{
    return tid % maxUserThread;
}


#endif
