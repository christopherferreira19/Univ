#include "syscall.h"

void print(void * arg) {

	int *n = (int*)arg;

	PutString("Printing from user thread print!");
	PutInt(*n);
	PutChar('\n');
}


 int main() {
 	int i =10;
 	PutString("Hello");

	UserThreadCreate(print, (void *) &i);
 	UserThreadJoin(0);

 	PutString("Goodbye");

 	return 0;
 }



