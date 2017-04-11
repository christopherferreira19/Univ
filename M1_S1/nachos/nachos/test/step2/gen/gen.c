#include "stdio.h"
#include "stdlib.h"
#include "time.h"

int main() {
	srand(time(NULL));
	for (int i = 0; i < 500; i++) {
		printf("%c", (int) (rand() * ('z' - 'a')) + 'a');
	}
	puts("");
	return 0;
}
