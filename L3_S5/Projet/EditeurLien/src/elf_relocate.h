#ifndef __ELF_RELOCATE_h__
#define __ELF_RELOCATE_h__

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include "elf_common.h"
#include "elf_rel_args.h"

// Read 'filename_src' as a REL ELF file and apply a relocation
// following the given arguments
// The result is written in the file 'filename_dst'
// and returned as a Elf_s
Elf_s elf_relocate(char* filename_src, char* filename_dst,
        int section_addr_size,
        Arg_Addr* section_addrs,
        char* entry_point);


#endif
