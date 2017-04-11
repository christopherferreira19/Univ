
#include "syscall.h"


#define MAXTHREAD 6

rwlock_t rwlock;
int read;
int write;

void TestRead(void *dummy)
{

    RWLockAcquireRead(&rwlock);
    read++;
    for(int i = 0;i< 100;i++)
    {
	PutInt(read);
	UserThreadYield();
	if(write != 0)
	    PutString("Write and Read in TestRead");
    }
    read--;
    RWLockRelease(&rwlock);

}



void TestWrite(void *dummy)
{

    RWLockAcquireWrite(&rwlock);
    write++;
    for(int i = 0;i< 100;i++)
    {
	UserThreadYield();
	if(read != 0)
	    PutString("Write and Read in TestWrite");
	if(write != 1)
	    PutString("multiple write");
    }
    write--;
    RWLockRelease(&rwlock);

}


int main()
{
    int tid[MAXTHREAD];

    RWLockInit(&rwlock);

    read = 0;
    write = 0;

    for(int i = 0; i<MAXTHREAD;i++)
    {
	if(i < MAXTHREAD/2)
	    tid[i] = UserThreadCreate(TestRead, NULL);
	else
	    tid[i] = UserThreadCreate(TestWrite, NULL);
    }


    for(int i = 0; i<MAXTHREAD;i++)
    {
	UserThreadJoin(tid[i]);

    }

    PutString("");
    PutString("OK!");

}
