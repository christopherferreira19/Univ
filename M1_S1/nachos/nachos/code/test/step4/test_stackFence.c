#include "syscall.h"


void dummy(void* arg)
{
    dummy((void*) 1);
}


int main()
{

    int id = UserThreadCreate(dummy, (void*)1);
    UserThreadJoin(id);

}
