#include <string.h>
#include "utils/csapp.h"
#include "http.h"
#include "logs.h"
#include "filtering.h"
#include "utils/strutils.h"
#include "server.h"
#include "utils/url_parser.h"
#include "filterWatch.h"

#define NPROC 16

bool reloadFilter;
int nproc;
int child[NPROC];

void sigchld(int signal) {
    pid_t pid;
    int status;
    while ((pid = waitpid(-1, &status, WNOHANG | WCONTINUED | WUNTRACED) > 0)) {
        if (WIFEXITED(status)) {
            printf("Exited\n");
            nproc--;
        }
        else if (WIFSIGNALED(status)) {
            printf("Signaled: %s\n", strsignal(WTERMSIG(status)));
            nproc--;
        }
        else if (WIFSTOPPED(status)) {
            printf("Stopped\n");
        }
        else if (WIFCONTINUED(status)) {
            printf("Continued\n");
        }
    }
}

void sigusr1(int signal) {
    for (int i = 0; i < NPROC; i++) {
        kill(child[i], SIGUSR1);
    }
}

void serve(int listenfd, bool serverproxy, char *serverhost, int serverport, int fdlogs, char *filterFileName);

void serveManager(int clientfd, char *partialUri);

void serveProxy(bool serverproxy, char *serverhost, int serverport, int fdlogs, int clientfd,
                struct Request *request, Filter *filter, struct sockaddr_in *clientaddr);

int main (int argc, char *argv[]) {
    //Parse arguments
    if (argc != 3 && argc != 5) {
        fprintf(stderr, "usage: %s <listen port> <configuration directory path> [server host server port] \n", argv[0]);
        exit(1);
    }

    int clientport = atoi(argv[1]);
    localDirPath = concat(argv[2]);
    watchDirPath = concat(localDirPath, "/config/watch");
    char* filterFileName = concat(localDirPath, "/config/watch/filterConfig.txt");
    char* logsFileName = concat(localDirPath, "/config/logs.txt");

    bool serverproxy = argc == 5;
    char* serverhost = NULL;
    int serverport = -1;
    if (serverproxy) {
        serverhost = argv[3];
        serverport = atoi(argv[4]);
    }

    // Set SERVER_PORT in environment variable
    // for python script(s) (e.g : logs.py)
    char* setServerPort = concat("SERVER_PORT=", argv[1]);
    putenv(setServerPort);

    int fdLogs = logsOpen(logsFileName);
    int listenfd = Open_listenfd(clientport);

    if (Fork() == 0) {
        watch(watchDirPath);
    }
    nproc = 1;

#if NPROC > 0
    Signal(SIGUSR1, sigusr1);

    int i;
    for (i = 0; i < NPROC; i++) {
        if ((child[i] = Fork()) == 0) {
            serve(listenfd, serverproxy, serverhost, serverport, fdLogs, filterFileName);
            exit(0);
        }
        nproc++;
    }
#endif

    Signal(SIGCHLD, sigchld);

#if NPROC == 0
    serve(listenfd, serverhost, serverport, fdLogs, filterFileName, serverproxy);
#else
    while (nproc > 0) sleep(0);
#endif

    Close(fdLogs);
    Close(listenfd);
    return 0;
}

void sigusr1serve(int signal) {
    reloadFilter = true;
}

void serve(int listenfd,
           bool serverproxy, char *serverhost, int serverport,
           int fdlogs, char *filterFileName) {
    Filter filter = parse_filter_config(filterFileName);
    reloadFilter = false;
    Signal(SIGUSR1, sigusr1serve);

    while (1) {
        struct sockaddr_in clientaddr;
        int clientlen = sizeof(clientaddr);
        int clientfd = Accept(listenfd, (SA *) &clientaddr, (socklen_t *) &clientlen);
        if (reloadFilter) {
            free_filter(&filter);
            filter = parse_filter_config(filterFileName);
            reloadFilter = false;
        }


        Request request = request_read(clientfd);

        char *partialUri;
        if ((partialUri = strstr(request->uri, "tinyproxy:manager")) != NULL) {
            serveManager(clientfd, partialUri);
        }
        else {
            serveProxy(serverproxy, serverhost, serverport, fdlogs, clientfd, request, &filter, &clientaddr);
        }

        request_free(request);
        Close(clientfd);
    }
}

void serveManager(int clientfd, char *partialUri) {
    struct stat sbuf;
    char filename[MAXLINE], cgiargs[MAXLINE];

    bool isStatic = parse_manager_args(partialUri, filename, cgiargs);
    if (stat(filename, &sbuf) < 0) {
                clienterror(clientfd, filename, "404", "Not found",
                            "Tinyproxy couldn't find this file");
            }
            else if (isStatic) { // Serve static content
                if (!(S_ISREG(sbuf.st_mode)) || !(S_IRUSR & sbuf.st_mode)) {
                    clienterror(clientfd, filename, "403", "Forbidden",
                                "Tinyproxy couldn't read the file");
                }
                else {
                    serve_static(clientfd, filename, (int) sbuf.st_size);
                }
            }
            else if (!(S_ISREG(sbuf.st_mode)) || !(S_IXUSR & sbuf.st_mode)) {
                clienterror(clientfd, filename, "403", "Forbidden",
                            "Tinyproxy couldn't run the CGI program");
            }
            else {
                serve_dynamic(clientfd, filename, cgiargs);
            }
}

void serveProxy(bool serverproxy, char *serverhost, int serverport, int fdlogs, int clientfd,
                struct Request *request, Filter *filter, struct sockaddr_in *clientaddr) {
    struct parsed_url *parsed = parse_url(request->uri);
    if (parsed == NULL) {
        clienterror(clientfd, request->uri, "418", "I’m a teapot",
                    "Tinyproxy couldn't resolve url");
    }
    else {
        int port = parsed->port == NULL
                   ? 80
                   : atoi(parsed->port);

        int serverfd = serverproxy
                       ? Open_clientfd(serverhost, serverport)
                       : Open_clientfd(parsed->host, port);
        if (strcmp(request->method, "HEAD") == 0 || strcmp(request->method, "GET") == 0) {
            if (authorized(filter, clientaddr, request)) {
                request_write(request, serverfd);
                Response response = response_read(serverfd);
                response_write(response, clientfd);

                // For debug Only
                //request_write(request, STDOUT_FILENO);
                //response_write(response, STDOUT_FILENO);
                logit(fdlogs, request, response->status_code, (*clientaddr));
                response_free(response);
            }
            else {
                clienterror(clientfd, request->uri,
                            "403", "Not Allowed",
                            "Tinyproxy can't allow that");
                logit(fdlogs, request, 403, (*clientaddr));
            }
        }
        else {
            clienterror(clientfd, request->uri,
                        "405", "Method Not Allowed",
                        "Tinyproxy does not support this HTTP method");
            logit(fdlogs, request, 403, (*clientaddr));
        }

        Close(serverfd);
    }
}
