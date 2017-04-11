#include "syscall.h"

//#define MAX_THREADS 5

sem_t mutex;

int C = 0;

void semaphore_increment(void* arg) {
	SemaphoreP(&mutex);

	int temp = C;
	PutString("Hello I am new thread");
	C = temp + 1;

	PutString("New value : ");
	PutInt(C);
	PutString("\n");
	SemaphoreV(&mutex);
}


int main() {
	int max_threads;    
	GetInt(&max_threads);
	int tid[max_threads];
	int i;
	//GetInt();
	
	//GetString(" MAX_THREADS : ");
	PutString("At the beginning\n");
	 SemaphoreInit(&mutex, 1);

	for(i = 0; i < max_threads; ++i){
		tid[i] = UserThreadCreate(semaphore_increment, 0);
	}

	for(i = 0; i < max_threads; ++i){
		UserThreadJoin(tid[i]);
	}

	SemaphoreDestroy(&mutex);
	PutString("At the end\n");

	return 0;
}