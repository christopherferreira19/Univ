#include "syscall.h"

#define ITERATION 100
#define TEST_THREAD_COUNT 9

mutex_t mutex;


void TestThread(void *tid)
{
    int it;

    for (it=0;it<100;it++)
    {
	;
    }
}


int main ()
{
    int tid[TEST_THREAD_COUNT];
    int i;

    int j;
    for(j=0;j<ITERATION;j++)
    {
	for(i=0;i<TEST_THREAD_COUNT;i++)
	{
	    tid[i] = UserThreadCreate(TestThread, (void*)i);
	}

	for(i=0;i<TEST_THREAD_COUNT;i++)
	{
	    PutInt(tid[i]);
	    PutString("");
	    int res = UserThreadJoin(tid[i]);
	    PutInt(res);
	    PutString("");	    
	}
    }
    return 0;
}
