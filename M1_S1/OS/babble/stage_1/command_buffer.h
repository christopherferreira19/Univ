#ifndef __COMMAND_BUFFER_H__
#define __COMMAND_BUFFER_H__

#include "babble_config.h"
#include "babble_types.h"

void command_buffer_init();

void add_command(command_t *cmd);

command_t* get_command();

#endif
