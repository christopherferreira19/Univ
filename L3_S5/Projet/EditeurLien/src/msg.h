#ifndef __MSG_H__
#define __MSG_H__

#include <stdbool.h>
#include <stdio.h>

typedef enum {
    NONE    = 0,
    ERROR   = 1,
    WARNING = 2,
    INFO    = 3
} Msg_Type;

extern Msg_Type msg_type;

void init_msg(Msg_Type type);

#define msg_error (msg_type >= ERROR)
#define msg_warn  (msg_type >= WARNING)
#define msg_info  (msg_type >= INFO)

#define info(...) if (msg_type >= INFO)    fprintf(stdout, __VA_ARGS__);
#define warn(...) if (msg_type >= WARNING) fprintf(stdout, __VA_ARGS__);
#define err(...)  if (msg_type >= ERROR)   fprintf(stderr, __VA_ARGS__);

#endif