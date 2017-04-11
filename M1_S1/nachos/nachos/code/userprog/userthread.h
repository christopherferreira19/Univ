#ifndef USER_THREAD_H
#define USER_THREAD_H

#include "system.h"
#include "userthreadmanager.h"

class UserThreadManager;

/* Unique id for a running thread */
typedef int ntid_t;


class UserThread
{
  private:
    Thread *sysThread;
    npid_t npid;
    ntid_t tid;
    int stackBegin;
    List *waitingForFinish;
    
  public:
    UserThread (char *name, npid_t npid, AddrSpace *space, ntid_t ntid, int stackBegin);
     ~UserThread ();
     void Initialize();
     npid_t getNpid();
     const char* getName();
     ntid_t getTid();
     void AddWaitingForFinish(Thread *thread);
     void WakeWaitingForFinish();
     Thread* getSystemThread();
     int getStackBegin();
     int getStackEnd();
     void Finish();
    };

typedef struct userTreadArg_t {
    int f;
    int arg;
    int ret;
    UserThread *userThread;
} UserThreadArg;


void SchedulerReadyToRun(int thread);

#endif
