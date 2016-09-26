#include "logs.h"

void setTime(Log *log) {
    time_t rawtime;
    struct tm localTime;

    rawtime = time(NULL);
    if (rawtime == ((time_t) -1))
        return;
    else
        localTime = *localtime(&rawtime);
    strftime(log->date, 128, "%a %d %b %Y %X %Z", &localTime);
}

void logit(int fdLogs, Request pRequest, int status_code, struct sockaddr_in addr_in) {
    Log log;
    char buf[MAXLINE];
    struct flock fl;

    setTime(&log);
    inet_ntop(AF_INET, &addr_in.sin_addr, log.clientIpStr, INET_ADDRSTRLEN);     //set client ip

    // Lock file
    fl = logsLock(fdLogs);
    int statusCode = status_code;
    sprintf(buf, "%s: %s http:/%s %d\n", log.date, log.clientIpStr, pRequest->uri, statusCode);
    Rio_writen(fdLogs, buf, strlen(buf));
    // Unlock file
    logsUnlock(fdLogs, fl);
}

struct flock logsLock(int fdLogs) {
    // l_type   l_whence  l_start  l_len  l_pid
    struct flock fl = {F_WRLCK, SEEK_SET, 0, 0, getpid()};

    //FGETFL macro to sets the file status flags to the value of the third argument
    if (fcntl(fdLogs, F_GETFL, &fl) == -1) {
        unix_error("fcntl");
    }

    return fl;
}

void logsUnlock(int fdLogs, struct flock fl) {
    fl.l_type = F_UNLCK;
    if (fcntl(fdLogs, F_SETLK, &fl) == -1) {
        unix_error("fcntl");
    }
}

int logsOpen(char* logsFileName) {
    int fdLogs;
    if ((fdLogs = open(logsFileName, O_CREAT | O_WRONLY | O_APPEND, S_IRWXU | S_IRWXG | S_IRWXO)) == -1) {
        unix_error(logsFileName);
    }
    return fdLogs;
}