#include "http.h"

char* trim(char* str) {
    while (*str != '\0' && *str == ' ') str++;
    size_t length = strlen(str);
    if (length > 0) {
        char* end = str + length - 1;
        while (*end == ' ' || *end == '\r' || *end == '\n') end--;
        *(end+1) = '\0';
    }

    return str;
}

void header_read(rio_t *rio, char *buf, Header *header) {
    int entries_count = 0;
    while (true) {
        ssize_t r = Rio_readlineb(rio, buf, MAXLINE);
        if (strcmp(buf, "\r\n") == 0) {
            break;
        }

        if (r > 0) {
            char* delim = strchr(buf, ':');
            *delim = '\0';
            header->entries[entries_count].name = strdup(trim(buf));
            header->entries[entries_count].value = strdup(trim(delim + 1));

            entries_count++;
        }
    }

    header->entries_count = entries_count;
}

Request request_read(int fd) {
    rio_t rio;
    char buf[MAXLINE];

    Rio_readinitb(&rio, fd);
    ssize_t r = Rio_readlineb(&rio, buf, MAXLINE);
    if (r == 0) {
        printf("Received empty request\n");
        return NULL;
    }

    Request request = malloc(sizeof(struct Request));
    sscanf(buf, "%s %s %s", request->method, request->uri, request->version);
    header_read(&rio, buf, &request->header);

    return request;
}

Response response_read(int fd) {
    rio_t rio;
    char buf[MAXLINE];

    Rio_readinitb(&rio, fd);
    ssize_t r = Rio_readlineb(&rio, buf, MAXLINE);
    if (r == 0) {
        printf("Received empty response\n");
        return NULL;
    }

    Response response = malloc(sizeof(struct Response));
    sscanf(buf, "%s %d %s", response->version, &response->status_code, response->status_name);
    header_read(&rio, buf, &response->header);

    HeaderEntry* content_length_entry = header_find_entry(&response->header, "Content-Length");
    int content_length = content_length_entry == NULL
            ? 0
            : atoi(content_length_entry->value);
    response->content_length = content_length;
    response->content = malloc(response->content_length * sizeof(char));

    char *content = response->content;
    while (content_length > 0) {
        r = Rio_readlineb(&rio, content, (size_t) content_length);
        if (r > 0) {
            content += r;
            content_length -= r;
        }
    }

    return response;
}

HeaderEntry* header_find_entry(Header* header, char* name) {
    for (int i = 0; i < header->entries_count; i++) {
        if (strcasecmp(name, header->entries[i].name) == 0) {
            return &(header->entries[i]);
        }
    }

    return NULL;
}

void header_write(Header header, char *buf, int fd) {
    for (int i = 0; i < header.entries_count; i++) {
        sprintf(buf, "%s: %s\r\n", header.entries[i].name, header.entries[i].value);
        Rio_writen(fd, buf, strlen(buf));
    }
}

void request_write(Request request, int fd) {
    char buf[MAXLINE];
    sprintf(buf, "%s %s %s\r\n", request->method, request->uri, request->version);
    Rio_writen(fd, buf, strlen(buf));
    header_write(request->header, buf, fd);
    Rio_writen(fd, "\r\n", 2);
}

void response_write(Response response, int fd) {
    char buf[MAXLINE];
    sprintf(buf, "%s %d %s\r\n", response->version, response->status_code, response->status_name);
    Rio_writen(fd, buf, strlen(buf));
    header_write(response->header, buf, fd);
    Rio_writen(fd, "\r\n", 2);
    Rio_writen(fd, response->content, (size_t) response->content_length);
}

void header_free(Header header) {
    for (int i = 0; i < header.entries_count; i++) {
        free(header.entries[i].name);
        free(header.entries[i].value);
    }
}

void request_free(Request request) {
    header_free(request->header);
    free(request);
}

void response_free(Response response) {
    header_free(response->header);
    free(response->content);
    free(response);
}