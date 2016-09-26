#include "filtering.h"
#include "utils/url_parser.h"

bool check_format_ipv4(char* ip);
bool check_format_uri(char* uri);
bool check_format_domain(char* domain);
void printFilterRuleShort();
void printFilterRule(const Filter *filter);

bool authorizedIp(Filter filter, struct sockaddr_in addr_in) {
    char clientIpStr[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &addr_in.sin_addr, clientIpStr, INET_ADDRSTRLEN);
    return (bool) !sm_exists(filter.sm_ip, clientIpStr);
}

bool autorizedUri(Filter filter, char *uri) {
    return (bool) !sm_exists(filter.sm_uri, uri);
}

bool authorizedDomain(Filter filter, char *uri) {
    struct parsed_url *parsed = parse_url(uri);
    if (parsed == NULL) return false;

    char *domain = parsed->host;
    while (domain != NULL && !sm_exists(filter.sm_domain, domain)) {
        domain = strchr(domain, '.');
        if (domain != NULL) {
            domain++;
        }
    }

    parsed_url_free(parsed);
    return domain == NULL;
}

bool authorized(Filter *filter, struct sockaddr_in *clientaddr, struct Request *request) {
    bool authorized = authorizedIp((*filter), (*clientaddr))
                      && autorizedUri((*filter), request->uri)
                      && authorizedDomain((*filter), request->uri);
    return authorized;
}

Filter new_filter(Filter *filter) {
    filter->sm_ip = sm_new(DEFAULT_HASHMAP_SIZE);
    filter->sm_uri = sm_new(DEFAULT_HASHMAP_SIZE);
    filter->sm_domain = sm_new(DEFAULT_HASHMAP_SIZE);
    return *filter;
}

void free_filter(Filter *filter) {
    sm_delete(filter->sm_ip);
    sm_delete(filter->sm_uri);
    sm_delete(filter->sm_domain);
}


Filter parse_filter_config(char *filterFileName) {
    static const char* flag_ip = "##CLIENT_IP";
    static const char* flag_uri = "##URI";
    static const char* flag_domain = "##DOMAIN";
    enum Parse {none, ip, uri, domain};
    enum Parse parse = none;

    int fdFilter;
    Filter filter = new_filter(&filter);

    size_t mapLength;
    char* mapStart = MMmap(filterFileName, &mapLength, &fdFilter);
    char* field[MAXLINE];
    unsigned long fieldLength[MAXLINE];

    char buf[MAXLINE];
    int nbField = 0;
    char* mapPredCursor = mapStart;
    for(char *mapCursor = mapStart; mapCursor < mapStart + mapLength ; mapCursor++) {
        char **currentFilter = &(field[nbField]);
        unsigned long *currentLength = &(fieldLength[nbField]);

        if (*mapCursor == '\n') {
            *currentFilter = mapPredCursor;
            *currentLength = mapCursor - mapPredCursor + 1;

            snprintf(buf, *currentLength, "%s", *currentFilter);
            if (strncmp (flag_ip, buf, *currentLength) == 0) {
                parse = ip;
            } else if (strncmp (flag_uri, buf, *currentLength) == 0) {
                parse = uri;
            } else if (strncmp (flag_domain, buf, *currentLength) == 0) {
                parse = domain;
            } else {
                switch (parse) {
                    case ip:
                        if (check_format_ipv4(buf))
                            sm_put(filter.sm_ip, buf, buf);
                        break;
                    case uri:
                        if (check_format_uri(buf))
                            sm_put(filter.sm_uri, buf, buf);
                        break;
                    case domain:
                        if (check_format_domain(buf))
                            sm_put(filter.sm_domain, buf, buf);
                        break;
                    default:
                        break;
                }
            }
            mapPredCursor = mapCursor + 1;
            nbField++;
        }
    }

    // For debug only
    printFilterRuleShort();
    //printFilterRule(&filter);

    MMunmap(mapStart, mapLength, fdFilter);
    return filter;
}

void printFilterRuleShort() {
    printf("Filer loaded, NEW RULES in %d\n", getpid());
}

static void iter(const char *key, const char *value, const void *obj) {
    printf("key: %s value: %s\n", key, value);
}

void printFilterRule(const Filter *filter) {
    printf("Filter loaded : NEW RULES\n");
    printf("---------------------------\n");
    sm_enum(filter->sm_ip, iter, NULL);
    sm_enum(filter->sm_uri, iter, NULL);
    sm_enum(filter->sm_domain, iter, NULL);
    printf("...........................\n");
    printf("Hashmap sm_ip entries = %d\n", sm_get_count(filter->sm_ip));
    printf("Hashmap sm_uri entries = %d\n", sm_get_count(filter->sm_uri));
    printf("Hashmap sm_domain entries = %d\n", sm_get_count(filter->sm_domain));
    printf("---------------------------\n");
}

char* MMmap(const char *filterFileConfigName, size_t *mapLength, int *fdFilt) {
    int fd;
    struct stat stats;
    char *p;

    // Get file size
    if (stat(filterFileConfigName, &stats))
        unix_error("Cannot stat file: ");

    if ((fd = open(filterFileConfigName, O_CREAT | O_RDONLY, S_IRWXU | S_IRWXG | S_IRWXO)) < 1)
        unix_error("Cannot open file: ");

    if (!(p = (char *)mmap(NULL, (size_t) stats.st_size, PROT_READ, MAP_SHARED, fd, 0))) {
        Close(fd);
        unix_error("Cannot map file: ");
    }

    *fdFilt = fd,
    *mapLength = (size_t) stats.st_size;

    return p;
}

void MMunmap (void* p, size_t mapLength, int fd) {
    Munmap(p, mapLength);
    Close(fd);
}

// Check the format of the rule
bool check_format_ipv4(char* ip) {
    struct sockaddr_in sa;
    int result = inet_pton(AF_INET, ip, &(sa.sin_addr));
    return result != 0;
}

bool check_format_uri(char* uri) {
    struct parsed_url *parsed = parse_url(uri);
    if (parsed != NULL) {
        parsed_url_free(parsed);
        return true;
    }
    return false;
}

bool check_format_domain(char* domain) {
    return strlen(domain) >= 2;
}
