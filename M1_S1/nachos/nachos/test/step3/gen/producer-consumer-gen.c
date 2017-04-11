#include "stdio.h"
#include "stdlib.h"
#include "time.h"

#define VALUE_MAX 1000
#define TIME_MAX 100
#define COUNT 28

double uniform_random() {
	double r = (double) rand();
	return r / (double) RAND_MAX;
}

int main() {
	srand(time(NULL));
	printf("%d\n", COUNT);
	for (int i = 0; i < COUNT; i++) {
		printf("%d\n%d\n", (int) (uniform_random() * TIME_MAX),
				(int) (uniform_random() * VALUE_MAX));
	}

	return 0;
}
