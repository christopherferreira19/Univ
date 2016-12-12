#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "buffer.h"

typedef struct 
{
	void* array[BABBLE_BUFFER_QUEUE_SIZE];
	int count;
	int start;

	sem_t not_full;
	pthread_cond_t not_empty;
} buffer_t;

pthread_mutex_t mutex;
buffer_t client_buffer;
buffer_t cmd_buffer;


void buffer_init(buffer_t* buf)
{
	pthread_mutex_init(&mutex, NULL);
	sem_init(&buf->not_full, 0, BABBLE_BUFFER_QUEUE_SIZE);
	pthread_cond_init(&buf->not_empty, NULL);
	buf->count=0;
	buf->start=0;  
}

void buffers_init()
{
	buffer_init(&client_buffer);
	buffer_init(&cmd_buffer);
}

void buffer_add_client(int* client)
{
	sem_wait(&client_buffer.not_full);
	pthread_mutex_lock(&mutex);

	int index = (client_buffer.start + client_buffer.count) % BABBLE_BUFFER_QUEUE_SIZE;
	client_buffer.array[index] = client;
	client_buffer.count++;

	pthread_cond_signal(&client_buffer.not_empty);

	pthread_mutex_unlock(&mutex);
}

void buffer_add_command(command_t* command)
{
	sem_wait(&cmd_buffer.not_full);
	pthread_mutex_lock(&mutex);

	int index = (cmd_buffer.start + cmd_buffer.count) % BABBLE_BUFFER_QUEUE_SIZE;
	cmd_buffer.array[index] = command;
	cmd_buffer.count++;

	pthread_cond_signal(&cmd_buffer.not_empty);
	pthread_cond_signal(&client_buffer.not_empty);
	pthread_mutex_unlock(&mutex);
}

void* buffer_get_client_or_command(bool* is_client)
{
	pthread_mutex_lock(&mutex);
	while (client_buffer.count == 0 && cmd_buffer.count == 0) {
		pthread_cond_wait(&client_buffer.not_empty, &mutex);
	}
	
	void* item;
	if (client_buffer.count > 0) {
		*is_client = true;
		item = client_buffer.array[client_buffer.start];
		client_buffer.start = (client_buffer.start + 1) % BABBLE_BUFFER_QUEUE_SIZE;
		client_buffer.count--;
	
		pthread_mutex_unlock(&mutex);
		sem_post(&client_buffer.not_full);
	}
	else {
		*is_client = false;
		item = cmd_buffer.array[cmd_buffer.start];
		cmd_buffer.start = (cmd_buffer.start + 1) % BABBLE_BUFFER_QUEUE_SIZE;
		cmd_buffer.count--;
	
		pthread_mutex_unlock(&mutex);
		sem_post(&cmd_buffer.not_full);
	}
	
	return item;
}

command_t* buffer_get_command()
{
	pthread_mutex_lock(&mutex);
	while (cmd_buffer.count == 0) {
		pthread_cond_wait(&cmd_buffer.not_empty, &mutex);
	}
	
	void* item = cmd_buffer.array[cmd_buffer.start];
	cmd_buffer.start = (cmd_buffer.start + 1) % BABBLE_BUFFER_QUEUE_SIZE;
	cmd_buffer.count--;
	
	pthread_mutex_unlock(&mutex);
	sem_post(&cmd_buffer.not_full);
	
	return item;
}
