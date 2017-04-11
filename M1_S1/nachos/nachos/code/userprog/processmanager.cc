#include "system.h"

#define MAX_PROCESSES 512

static inline int NPID_TO_INDEX(npid_t pid) {
    return pid % MAX_PROCESSES;
}

ProcessManager::ProcessManager() {
    nextNpid = 0;
    nextToRun = 0;
    array = new Process*[MAX_PROCESSES];
}

ProcessManager::~ProcessManager() {
    delete [] array;
}

Process *ProcessManager::CreateProcess(char* filename) {
    npid_t newNpid = getNewNpid();
    Process *process = new Process(newNpid, filename);
    if (process->getAddrSpace() == NULL) {
        return NULL;
    }

    array[NPID_TO_INDEX(newNpid)] = process;
    processCount++;
    return process;
}


npid_t ProcessManager::getNewNpid()
{
    while (array[NPID_TO_INDEX(nextNpid)] != NULL) {
        nextNpid++;
    }
    return nextNpid;
}

Process *ProcessManager::getProcess(npid_t npid) {
    Process* process = array[NPID_TO_INDEX(npid)];
    return process != NULL && process->getNpid() == npid
            ? process
            : NULL;
}


int ProcessManager::ExitProcess(npid_t npid, int exitStatus) {
    DEBUG('t', "Deleting process %u\n", npid);
    Process *toDelete = array[NPID_TO_INDEX(npid)];
    ASSERT(toDelete != NULL);
    ASSERT(toDelete->getNpid() == npid);

    toDelete->Finish(exitStatus);
    delete toDelete;
    array[NPID_TO_INDEX(npid)] = NULL;

    processCount--;
    DEBUG('t', "%d process left\n", processCount);
    return processCount;
}


void ProcessManager::Print() {
    int i;
    for(i=0;i<MAX_PROCESSES;i++)
    {
	if(array[i] != NULL) {
	    DEBUG('t', "Process %d [%p]\n", array[i]->getNpid(), array[NPID_TO_INDEX(i)]);
	}
		
    }
}
