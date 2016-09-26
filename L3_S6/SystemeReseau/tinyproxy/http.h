#ifndef TINYPROXY_HTTPHEADER_H
#define TINYPROXY_HTTPHEADER_H

#include <stdbool.h>
#include "utils/csapp.h"

#define MAXENTRIES 60

typedef struct {
    char* name;
    char* value;
} HeaderEntry;

typedef struct {
    int entries_count;
    HeaderEntry entries[MAXENTRIES];
} Header;

struct Request {
    char method[MAXLINE];
    char uri[MAXLINE];
    char version[MAXLINE];
    Header header;
};

typedef struct Request* Request;

struct Response {
    char version[MAXLINE];
    int status_code;
    char status_name[MAXLINE];
    Header header;
    int content_length;
    char* content;
};

typedef struct Response* Response;

Request request_read(int fd);
void request_write(Request request, int fd);
void request_free(Request request);

Response response_read(int fd);
void response_write(Response response, int fd);
void response_free(Response reponse);

HeaderEntry* header_find_entry(Header* header, char* name);

#endif
