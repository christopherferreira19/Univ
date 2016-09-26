#include <time.h>
#include "http.h"
#include <netinet/in.h>

#ifndef TINYPROXY_LOG_H
#define TINYPROXY_LOG_H

typedef struct {
    char date[MAXLINE];
    char clientIpStr[INET_ADDRSTRLEN];
} Log;

void setTime(Log *log);
void logit(int fdLogs, Request pRequest, int status_code, struct sockaddr_in addr_in);
struct flock logsLock(int fdLogs);
void logsUnlock(int fdLogs, struct flock fl);
int logsOpen(char* logsFileName);

#endif //TINYPROXY_LOG_H
