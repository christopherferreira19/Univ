#ifndef TINYPROXY_STRUTILS_H
#define TINYPROXY_STRUTILS_H

char* r_concat (char* arg, ...);
#define concat(...) r_concat(__VA_ARGS__, "\0");

#endif //TINYPROXY_STRUTILS_H
