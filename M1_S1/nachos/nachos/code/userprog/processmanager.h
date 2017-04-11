#ifndef __PROCESS_MANAGER_H__
#define __PROCESS_MANAGER_H__

#include "process.h"

class ProcessManager {
public:
    ProcessManager();
    ~ProcessManager();
    Process *CreateProcess(char* filename);
    Process *CreateProcess(char* filename, Thread *main);
    Process *getProcess(npid_t npid);
    int ExitProcess(npid_t npid, int exitStatus);
private:
    npid_t nextNpid;
    npid_t nextToRun;
    npid_t processCount;
    Process **array;
    npid_t getNewNpid();
    void Print();
};

#endif
