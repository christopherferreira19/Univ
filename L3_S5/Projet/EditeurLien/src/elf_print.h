#ifndef __ELF_PRINT_H_
#define __ELF_PRINT_H_

#include "elf_common.h"

void elf_print(Elf_s elf);
void elf_print_header(Elf_s elf);
void elf_print_section_header(Elf_s elf);
void elf_print_symbols(Elf_s elf);
void elf_print_relocation_header();
void elf_print_relocations_at(Elf_s elf, int index);
void elf_print_relocations(Elf_s elf, Elf32_Shdr* section_header);
void elf_print_relocation_footer();

#endif
