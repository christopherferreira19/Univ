#include "syscall.h"

#define ITERATION 1
#define TEST_THREAD_COUNT 9

mutex_t mutex;


int fibo(int n)
{
    if(n == 1 || n == 0)
	return 1;
    if(n < 0)
	return 0;
    return fibo(n - 1) + fibo(n - 2);
}

void TestThread(void *tid)
{
    int id = (int)tid;

    int res = fibo(id);
    LockAcquire(&mutex);
    PutString("\nFibo of:");
    PutInt(id);
    PutString(" is:");
    PutInt(res);
    LockRelease(&mutex);
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
	    UserThreadJoin(tid[i]);
	}
	PutString("");
    }
    return 0;
}
