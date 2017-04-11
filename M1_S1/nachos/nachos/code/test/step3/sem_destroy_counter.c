#include "syscall.h"

#define N_COUNTER 5
#define ITERATIONS 5000

sem_t sem;
int c;
int counter[N_COUNTER];


void count(void* args){

	int i=0;

	for(i=0; i<ITERATIONS; i++){
		SemaphoreP(&sem);
		int tmp = c;
		UserThreadYield();
		c = tmp + 1;
		
		SemaphoreV(&sem);	
	}
}

int main(){

	int i;

	c = 0;

	PutString("\nStarting program:\n");

	//SemaphoreInit(&sem, 1);
	SemaphoreDestroy(&sem);
	
	//Semaphore will work after reinitialization
	SemaphoreInit(&sem, 1);

	for(i=0; i<N_COUNTER; i++)
		counter[i] = UserThreadCreate(count, (void*)i);

	for(i=0; i<N_COUNTER; i++)
		UserThreadJoin(counter[i]);

	SemaphoreDestroy(&sem);

	PutString("Counter value: ");
	PutInt(c);
	PutString("\n");

	return 0;
}