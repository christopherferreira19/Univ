#include "syscall.h"

#define ITERATION 100
#define TEST_THREAD_COUNT 9

mutex_t mutex;


void TestThread(void *tid)
{
    int it;
    int id = (int)tid;

    for (it=0;it<100;it++)
    {
	LockAcquire(&mutex);
    	PutString("\nThread :");
    	PutInt(id);PutInt(it);
	LockRelease(&mutex);
    }
}


int main ()
{
    int tid[TEST_THREAD_COUNT];
    int i;
    LockInit(&mutex);

    int j;
    for(j=0;j<ITERATION;j++)
    {
	for(i=0;i<TEST_THREAD_COUNT;i++)
	{
	    tid[i] = UserThreadCreate(TestThread, (void*)i);
	}

	for(i=0;i<TEST_THREAD_COUNT;i++)
	{
	    LockAcquire(&mutex);
	    PutString("");
	    PutInt(tid[i]);
	    PutString("");
	    LockRelease(&mutex);
	    int res = UserThreadJoin(tid[i]);
	    PutInt(res);
	}
    }
    return 0;
}
