#include "syscall.h"

#define ITERATION 100
#define TEST_THREAD_COUNT 20

mutex_t mutex;


void TestThread(void *tid)
{
    int it;
    int id = (int)tid;

    for (it=0;it<ITERATION;it++)
    {
	LockAcquire(&mutex);
    	PutString("\nThread :");
    	PutInt(id);
	PutChar('-');
	PutInt(it);
    	PutString("");
	LockRelease(&mutex);
    }
}


int main ()
{
    int tid = 0;
    int max = 0;

    LockInit(&mutex);

    while(tid != -1)
    {
	tid = UserThreadCreate(TestThread, (void*)max);
	max++;
	UserThreadYield();
    }

    PutString("");
    PutString("Max thread : ");
    PutInt(max);
    PutString("");
    return 0;
}
