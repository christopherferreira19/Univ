// Send a message to an other nachOS
// must be called with -rs <nb> -m 1

#include "syscall.h"

int main()
{
    const char *msg = "Hello!";
    net_t network;

    ReliableTranfertCreate(&network, 0);

    
    Connect(&network);
    Send(&network, (char*)msg);

    
    /* Close(&network); */
    
    /* ReliableTranfertDestroy(&network); */
}

