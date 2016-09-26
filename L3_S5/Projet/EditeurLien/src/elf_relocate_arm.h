#ifndef __ELF_RELOCATE_ARM_H__
#define __ELF_RELOCATE_ARM_H__

#include <stdbool.h>
#include <stdint.h>
#include "elf_common.h"

bool elf_relocate_arm(Elf_s elf,
        Elf32_Shdr* section_header,
        Elf32_Sym* symbol,
        Elf32_Rel* relocation);

#endif
