// Recieve a message from an other nachOS
// must be called with -rs <nb> -m 0

#include "syscall.h"


int main()
{
    char data[20];
    
    net_t network;

    ReliableTranfertCreate(&network, 1);

    WaitForConnection(&network);
    Receive(&network, data);
    PutString(data);
    
    /* Close(&network); */
    
    /* ReliableTranfertDestroy(&network); */
}

