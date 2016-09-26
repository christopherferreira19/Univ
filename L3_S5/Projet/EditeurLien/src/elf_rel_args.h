#ifndef __ELF_REL_ARGS_H__
#define __ELF_REL_ARGS_H__

#include <stdbool.h>
#include "elf_common.h"

// This structure holds the address arguments passed
// to the relocation program from the command line
// (for example argument of the form "-s .text=0x58")
typedef struct {
    char* section; // .text
    int addr;      // 0x60
} Arg_Addr;

bool read_relocation_args(int argc, char** argv,
        char** file_src_param,
        char** file_res_param,
        int* section_addr_size,
        Arg_Addr** section_addr,
        char **entry_point);

void section_addrs_free(int section_addr_size, Arg_Addr* section_addrs);

// Given a name and a list of Arg_Addr
// find the first whose name match
int find_section_addr(char* name,
        int section_addr_size,
        Arg_Addr* section_addrs);

#endif