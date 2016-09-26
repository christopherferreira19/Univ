#include "strutils.h"
#include "csapp.h"
#include <stdarg.h>

char* base_concat(char* s1, char* s2) {
    size_t len1 = strlen(s1);
    size_t len2 = strlen(s2);
    char* result = malloc(len1+len2+1);
    memcpy(result, s1, len1);
    memcpy(result+len1, s2, len2+1);
    return result;
}

char* r_concat (char* arg, ...) {
    if (!strcmp(arg, "\0")) return "r_concat_arg_error\n";
    va_list va;
    va_start (va, arg);

    char *str = arg;
    char *str_c = va_arg (va, char*);

    while (strcmp(str_c, "\0")) {
        str = base_concat(str, str_c);
        str_c = va_arg (va, char*);
    }

    va_end (va);
    return str;
}