#ifndef __COMMAND_BUFFER_H__
#define __COMMAND_BUFFER_H__

#include <pthread.h>
#include <semaphore.h>
#include <stdbool.h>

#include "babble_config.h"
#include "babble_types.h"

void buffers_init();

void buffer_add_client(int* client);

void buffer_add_command(command_t* cmd);

void* buffer_get_client_or_command(bool* is_client);

command_t* buffer_get_command();

#endif
