#include <stdlib.h>
#include <string.h>
#include "elf_common.h"
#include "msg.h"
#include "elf_rel_args.h"

bool read_section_addr(char* arg, Arg_Addr* res) {
    int equal_index = 0;
    while (arg[equal_index] != '=' && arg[equal_index] != '\0') {
        equal_index++;
    }

    if (arg[equal_index] != '=') {
        err("Invalid section format '%s' (<section>=<address>)\n", arg);
        return false;
    }

    char* start = arg + equal_index + 1;
    char* end = start;
    res->addr = strtol(start, &end, 16);
    if (*start == '\0' || *end != '\0') {
        err("Invalid address '%s', must be an hexadecimal number\n", arg);
        return false;
    }

    char* section_name = malloc(equal_index + 1);
    strncpy(section_name, arg, equal_index);
    section_name[equal_index] = '\0';
    res->section = section_name;

    return true;
}

bool read_relocation_args(int argc, char** argv,
        char** file_src_param,
        char** file_dst_param,
        int* section_addr_size_param,
        Arg_Addr** section_addr_param,
        char **entry_point_param) {
    char* file_src = NULL;
    char* file_dst = NULL;
    int section_addr_size = 0;
    Arg_Addr* section_addr = malloc(argc * sizeof(Arg_Addr));
    char* entry_point = NULL;

    for (int i = 1; i < argc; i++) {
        char *arg = argv[i];
        if (arg[0] == '-') {
            if ((arg[1] != 's' && arg[1] != 'e') || arg[2] !='\0') {
                err("Invalid Flag '%s'\n", arg);
                free(section_addr);
                return false;
            }
            i++;
            if (i >= argc) {
                err("Flag '%s' must be followed by an argument\n", arg);
                free(section_addr);
                return false;
            }

            if (arg[1] == 's'){
                if (!read_section_addr(argv[i], section_addr + section_addr_size)) {
                    free(section_addr);
                    return false;
                }
                section_addr_size++;
            }
            else if (arg[1] == 'e') {
                if (entry_point != NULL) {
                    err("Only one entry point can be specified\n");
                    free(section_addr);
                    return false;
                }

                entry_point = argv[i];
            }
        }
        else {
            if (file_src == NULL) {
                file_src = arg;
            }
            else if (file_dst == NULL) {
                file_dst = arg;
            }
            else {
                free(section_addr);
                return false;
            }
        }
    }

    if (file_src == NULL) {
        err("Missing source file argument\n");
        free(section_addr);
        return false;
    }
    if (file_dst == NULL) {
        err("Missing destination file argument\n");
        free(section_addr);
        return false;
    }
    if (entry_point == NULL) {
        err("Error, no entry point specified\n");
        free(section_addr);
        return false;
    }

    *file_src_param = file_src;
    *file_dst_param = file_dst;
    *section_addr_size_param = section_addr_size;
    *section_addr_param = section_addr;
    *entry_point_param = entry_point;

    return true;
}

void section_addrs_free(int section_addr_size, Arg_Addr* section_addrs) {
    for (int i = 0; i < section_addr_size; i++) {
        free(section_addrs[i].section);
    }
    free(section_addrs);
}

int find_section_addr(char* name,
        int section_addr_size,
        Arg_Addr* section_addrs) {
    for (int j = 0; j < section_addr_size; j++) {
        if (!strcmp(section_addrs[j].section, name)) {
            return section_addrs[j].addr;
        }
    }

    return -1;
}
