#ifndef _PROCESS_H
#define _PROCESS_H

typedef int npid_t;

class Process;

#ifdef STEP3
#include "userthreadmanager.h"
class UserThreadManager;
#endif

#include "addrspace.h"
#include "thread.h"

#ifdef STEP4
void StartProcess(int arg);
#endif

class Process
{
  private:
    npid_t npid;
    AddrSpace *space;

  public:
    Process(npid_t npid_arg, char *filename);
    void Finish(int exitStatus);
    ~Process();
    npid_t getNpid();
    AddrSpace *getAddrSpace();
    void Run();

#ifdef STEP3
private:
    UserThreadManager *userThreadManager;
    int maxUserThread;
public:
    UserThreadManager *getUserThreadManager();
    void Schedule();
#endif

#ifdef STEP4
private:
    List *awaiting;
public:
    void AddAwaiting(Thread* thread, int *exitStatusAddr);
    void WakeAwaiting(int exitStatus);
#endif

#ifdef STEP5
private:
    // We store the sector of the current directory
    int currentDirectorySector;
public:
    int getCurrentDirectorySector();
    void setCurrentDirectorySector(int currentDirectory);
#endif
};

#ifdef STEP4
npid_t do_ForkExec(char* filename);
void do_WaitProcess(npid_t npid, int exitStatusAddr);
#endif

#endif
