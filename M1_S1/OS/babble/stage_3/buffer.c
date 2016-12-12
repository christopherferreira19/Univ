#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "buffer.h"

void buffer_init(buffer_t* buf)
{
	pthread_mutex_init(&buf->mutex, NULL);
	sem_init(&buf->not_full, 0, BABBLE_CMD_BUFFER_SIZE);
	sem_init(&buf->not_empty, 0, 0);
	buf->count=0;
	buf->start=0;  
}

void buffer_add_item(buffer_t* buf, void* item)
{
	sem_wait(&buf->not_full);
	pthread_mutex_lock(&buf->mutex);

	int index = (buf->start + buf->count) % BABBLE_CMD_BUFFER_SIZE;
	buf->array[index] = item;
	buf->count++;
	
	pthread_mutex_unlock(&buf->mutex);
	sem_post(&buf->not_empty);
}

void* buffer_get_item(buffer_t* buf)
{
	sem_wait(&buf->not_empty);
	pthread_mutex_lock(&buf->mutex);
	
	void* item = buf->array[buf->start];
	buf->start = (buf->start + 1) % BABBLE_CMD_BUFFER_SIZE;
	buf->count--;
	
	pthread_mutex_unlock(&buf->mutex);
	sem_post(&buf->not_full);
	
	return item;
}
