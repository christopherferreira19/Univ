#include "filtering.h"
#include "utils/strutils.h"

bool validConfig(char * filterFileName, char* error);


int main (void) {

    char cwd[MAXLINE];
    char valid_cwd[MAXLINE];
    char error[MAXLINE];


    // Format path to filter configuration file
    if (getcwd(cwd, sizeof(cwd)) == NULL)
        return 1;

    char* cwdp;
    cwdp = strstr(cwd, "/cgi-bin");
    if (cwdp == NULL) {
        printf("Wrong path %s\n", cwd);
        return 1;
    } else {
        size_t toRemove = strlen(cwdp);
        strncpy(valid_cwd, cwd, (strlen(cwd) - toRemove));
    }

    char* filterFileName = concat(valid_cwd, "/config/filterConfigTEMP.txt");

    // Check the new filter
    if (!validConfig(filterFileName, error)) {
        fprintf(stdout, "%s\n", error);
        return 1;
    }

    return 0;
}

bool isNewLine(char *line) {
    return strcmp(line, "") == 0;
}

bool validConfig(char * filterFileName, char* error) {
    static const char* flag_ip = "##CLIENT_IP";
    static const char* flag_uri = "##URI";
    static const char* flag_domain = "##DOMAIN";
    enum Parse {none, ip, uri, domain};
    enum Parse parse = none;

    int fdFilter;

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
                        if (!check_format_ipv4(buf) && !isNewLine(buf)) {
                            strcpy(error, buf);
                            return false;
                        }
                        break;
                    case uri:
                        if (!check_format_uri(buf) && !isNewLine(buf)) {
                            strcpy(error, buf);
                            return false;
                        }
                        break;
                    case domain:
                        if (!check_format_domain(buf) && !isNewLine(buf)) {
                            strcpy(error, buf);
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
            mapPredCursor = mapCursor + 1;
            nbField++;
        }
    }

    MMunmap(mapStart, mapLength, fdFilter);
    return true;
}
