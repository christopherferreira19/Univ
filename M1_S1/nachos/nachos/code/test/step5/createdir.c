#include "syscall.h"

int main(){

	if(CreateDirectory("new_dir"))
		PutString("created dir new_dir");

	return 0;
}