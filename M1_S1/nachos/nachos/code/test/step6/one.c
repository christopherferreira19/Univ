// Send a message to an other thread - on the same machine
// must be called with -rs <nb> -m 0

#include "syscall.h"


void ThreadSend(void* network)
{
    const char *msg = "Hello!";

    Connect(&network);
    Send(&network, (char*)msg);
}


int main()
{
    char data[20];
    
    net_t networkFrom;
    net_t networkTo;

    ReliableTranfertCreate(&networkFrom, 0);
    ReliableTranfertCreate(&networkTo, 0);
    
    int tid = UserThreadCreate(ThreadSend, (void*)networkTo);


    WaitForConnection(&networkFrom);
    Receive(&networkFrom, data);
    PutString(data);
    
    Close(&networkFrom);
    Close(&networkTo);
    
    ReliableTranfertDestroy(&networkFrom);
    ReliableTranfertDestroy(&networkTo);

    UserThreadJoin(tid);
    
}

