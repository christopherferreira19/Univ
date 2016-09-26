#ifndef _URL_PARSER_H
#define _URL_PARSER_H

/*
 * URL storage
 */
struct parsed_url {
    char *scheme;               /* mandatory */
    char *host;                 /* mandatory */
    char *port;                 /* optional */
    char *path;                 /* optional */
    char *query;                /* optional */
    char *fragment;             /* optional */
    char *username;             /* optional */
    char *password;             /* optional */
};


/*
 * Declaration of function prototypes
 */
struct parsed_url * parse_url(const char *);
void parsed_url_free(struct parsed_url *);


#endif /* _URL_PARSER_H */