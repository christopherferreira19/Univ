#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <elf.h>
#include <string.h>

#include "msg.h"
#include "elf_common.h"
#include "elf_print.h"
#include "elf_relocate.h"
#include "elf_rel_args.h"

int main (int argc, char** argv) {
    init_msg(WARNING);

    char* entry_point = NULL;
    char* file_src;
    char* file_dst;
    int section_addr_size;
    Arg_Addr* section_addrs;

    if (!read_relocation_args(argc, argv,
            &file_src,
            &file_dst,
            &section_addr_size,
            &section_addrs,
            &entry_point)) {
        err("Usage: %s [-e <symbol>] [-s <section>=<address> *] <source file> <output file>\n", argv[0]);
        err("    -e <symbol>            : Symbol to use as the program entry point\n");
        err("    -s <section>=<address> : Address to use for the section\n");
        err("    <source file>          : Object file to relocate\n");
        err("    <destination file>     : Destination file to which the resulting executable will be written\n");
        return 1;
    }

    Elf_s elf = elf_relocate(
            file_src,
            file_dst,
            section_addr_size,
            section_addrs,
            entry_point);
    if (elf == NULL) {
        return 2;
    }

    elf_free(elf);

    return 0;
}
