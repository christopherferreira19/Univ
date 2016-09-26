#ifndef TINYPROXY_SERVER_H
#define TINYPROXY_SERVER_H
#include <stdbool.h>
#include "globalPath.h"


// From tiny.c with some parsing modification
void clienterror(int fd, char *cause, char *errnum,
                 char *shortmsg, char *longmsg);

bool parse_manager_args(char *uri, char *filename, char *cgiargs);

void serve_static(int fd, char *filename, int filesize);

void serve_dynamic(int fd, char *filename, char *cgiargs);

#endif //TINYPROXY_SERVER_H
