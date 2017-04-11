// This sample shows if the current directory
// is per thread or per process

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

void print_cwd(char* name) {
    char cwd[1024];
    getcwd(cwd, 1024);
    printf("%-10s %s\n", name, cwd);
}

void child(int in) {
    char c;
    while (read(in, &c, 1) == 0);

    print_cwd("Child");
}

pthread_barrier_t barrier;

void* thread_start(void* arg) {
    print_cwd("Thread1");
	pthread_barrier_wait(&barrier);
	pthread_barrier_wait(&barrier);
    print_cwd("Thread2");

    return NULL;
}

void parent(int out) {
    pthread_t thread;

	pthread_barrier_init(&barrier, NULL, 2);
    pthread_create(&thread, NULL, thread_start, NULL);
	pthread_barrier_wait(&barrier);
    chdir("/");
	pthread_barrier_wait(&barrier);

    while (write(out, "!", 1) == 0);

    print_cwd("Parent");

    pthread_join(thread, NULL);
}

int main() {
    print_cwd("Start");

    int p[2];
    pipe(p);

    if (fork() == 0) {
        close(p[1]);
        child(p[0]);
    }
    else {
        close(p[0]);
        parent(p[1]);
    }
}