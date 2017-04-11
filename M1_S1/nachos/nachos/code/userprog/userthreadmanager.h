#ifdef STEP3
#ifndef USER_THREAD_MANAGER_H
#define USER_THREAD_MANAGER_H

#include "list.h"
#include "thread.h"
#include "userthread.h"

class UserThreadManager
{
  private:
    UserThread **threadArray;
    npid_t npid;
    AddrSpace *space;
    int threadCount;
    ntid_t nextTid;
    int stackOffset;
    int stackBase;
    ntid_t getNewTid();
    int getIdfromTid(ntid_t tid);
    int maxUserThread;
    
public:
    UserThreadManager (npid_t npid_arg, AddrSpace *addrspace, int maxThread);
     ~UserThreadManager ();
     void Initialize(Thread* main);
     UserThread* CreateUserThread();
     UserThread* getUserThread(ntid_t tid);
     void DeleteThread(ntid_t tid, int exitStatus);
     void Print();
};


#endif
#endif
