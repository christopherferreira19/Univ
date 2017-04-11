#include <stdio.h>
#include <pthread.h>

pthread_barrier_t barrier;

void* run(void* arg) {
	FILE* out = (FILE*) arg;

	pthread_barrier_wait(&barrier);
	
	char buf[256];
	while (fgets(buf, 256, stdin) != NULL) {
		fputs(buf, out);
	}

	return NULL;
}

#define N 5

int main() {
  	pthread_t threads[N];
	FILE* outs[N];

	pthread_barrier_init(&barrier, NULL, N);
	
	char fname[10];
	for (int i = 0; i < N; i++) {
	  snprintf(fname, 6, "out%d", i);
	  outs[i] = fopen(fname, "w");
	  pthread_create(&threads[i], NULL, run, outs[i]);
	}

	for (int i = 0; i < N; i++) {
	  pthread_join(threads[i], NULL);
	  fclose(outs[i]);
	}
	
	return 0;
}
