#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#include "command_buffer.h"

typedef struct
{
	command_t* array[BABBLE_CMD_BUFFER_SIZE];
	int count;
	int start;

	pthread_mutex_t mutex;
	sem_t empty;
	sem_t full;

} command_buffer_t;

command_buffer_t cmd_buf;

void command_buffer_init()
{
	pthread_mutex_init(&cmd_buf.mutex, NULL);
	sem_init(&cmd_buf.empty, 0, BABBLE_CMD_BUFFER_SIZE);
	sem_init(&cmd_buf.full, 0, 0);
	cmd_buf.count=0;
	cmd_buf.start=0;  
}

void add_command(command_t *cmd)
{
	sem_wait(&cmd_buf.empty);
	pthread_mutex_lock(&cmd_buf.mutex);

	int index = (cmd_buf.start + cmd_buf.count) % BABBLE_CMD_BUFFER_SIZE;
	cmd_buf.array[index] = cmd;
	cmd_buf.count++;
	
	pthread_mutex_unlock(&cmd_buf.mutex);
	sem_post(&cmd_buf.full);
}

command_t* get_command()
{
	sem_wait(&cmd_buf.full);
	pthread_mutex_lock(&cmd_buf.mutex);
	
	command_t* cmd = cmd_buf.array[cmd_buf.start];
	cmd_buf.start = (cmd_buf.start + 1) % BABBLE_CMD_BUFFER_SIZE;
	cmd_buf.count--;
	
	pthread_mutex_unlock(&cmd_buf.mutex);
	sem_post(&cmd_buf.empty);
	
	return cmd;
}
