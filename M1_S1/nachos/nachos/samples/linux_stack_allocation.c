#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdint.h>

int main_sp;
void* thread_run(void* arg) {
  void* p = NULL;
  int64_t sp = (int64_t) (void*) &p;
  printf("%d > %p\n", (int64_t) arg, main_sp - sp);
  sleep(5);
}

int main() {
  void* p = NULL;
  main_sp = (int64_t) (void*) &p;
  for (int64_t i = 0; i < 100; i++) {
    pthread_t t;
    pthread_create(&t, NULL, thread_run, (void*) i);
  }

  return 0;
}
