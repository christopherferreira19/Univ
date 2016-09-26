#ifndef TINYPROXY_FILTERING_H
#define TINYPROXY_FILTERING_H
#include "utils/csapp.h"
#include "logs.h"
#include "utils/strmap.h"
#include "globalPath.h"

#define DEFAULT_HASHMAP_SIZE 512

typedef struct {
    StrMap *sm_ip;
    StrMap *sm_uri;
    StrMap *sm_domain;
} Filter;

Filter parse_filter_config(char *filterFileName);
bool authorized(Filter *filter, struct sockaddr_in *clientaddr, struct Request *request);
void free_filter(Filter *filter);

// Shared with validConfigUpdate
char* MMmap(const char *filterFileConfigName, size_t *mapLength, int *fdFilt);
void MMunmap (void* p, size_t mapLength, int fd);
bool check_format_ipv4(char* ip);
bool check_format_uri(char* uri);
bool check_format_domain(char* domain);

#endif //TINYPROXY_FILTERING_H
