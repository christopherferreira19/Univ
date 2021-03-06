#ifndef GET_SHM
#define GET_SHM

#include <pthread.h>

struct shared_state {
   int refcount;
   int next_core;
   pthread_mutex_t pin_lock;
   int verbose;
   int verbose_err;
   int server;
   int server_fd;
   int per_node;
   int nr_entries_in_cores;
   int active;
   int initially;
   int cores[]; // must be the last field
};

struct shared_state *create_shm(char *id, struct shared_state *content, int *cores);
struct shared_state *restore_shm(char *id, char *size);
void cleanup_shm(char *id);

struct shared_state *get_shm(void);
char *get_shm_size(void);
int get_next_core(void);

void lock_shm(void);
void unlock_shm(void);

#endif
