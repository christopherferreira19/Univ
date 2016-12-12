#ifndef __COMMAND_BUFFER_H__
#define __COMMAND_BUFFER_H__

#include <semaphore.h>

#include "babble_config.h"
#include "babble_types.h"

typedef struct 
{
	void* array[BABBLE_CMD_BUFFER_SIZE];
	int count;
	int start;

	pthread_mutex_t mutex;
	sem_t not_empty;
	sem_t not_full;

} buffer_t;

void buffer_init(buffer_t* buf);

void buffer_add_item(buffer_t* buf, void* cmd);

void* buffer_get_item(buffer_t* buf);

#endif
