#include "syscall.h"

//#define MAX_THREADS 5

//sem_t mutex;

int C = 0;

void safe_increment(void* arg) {
//	SemaphoreP(&mutex);

	int temp = C;
	PutString("Hello I am new thread");
	PutChar('\n');
	C = temp + 1;

	PutString("New value : ");
	PutInt(C);
	PutChar('\n');
//	SemaphoreV(&mutex);

	UserThreadExit();
}


int main() {
	int max_threads;    
	GetInt(&max_threads);
	ntid_t tid[max_threads];
	int i;
	//int tid[MAX_THREADS], i;
	PutString("At the beginning\n");
//	SemaphoreInit(&mutex, 1);

	for (i = 0; i < max_threads; ++i){
		tid[i] = UserThreadCreate(safe_increment, 0);
	}

	for (i = 0; i < max_threads; ++i){
		UserThreadJoin(tid[i]);
	}

//	SemaphoreDestroy(&mutex);
	PutString("At the end\n");

	return 0;
}