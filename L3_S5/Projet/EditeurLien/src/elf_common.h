#ifndef __ELF_REL_H__
#define __ELF_REL_H__

#include <elf.h>
#include <stdio.h>
#include <stdbool.h>
#include "endianess.h"

#define SHF_ENTRY_POINT 2

#define R_ARM_ABS32   2
#define R_ARM_ABS16   5
#define R_ARM_ABS8    8
#define R_ARM_CALL   28
#define R_ARM_JUMP24 29

struct Elf_s {
    FILE*       file;

    Elf32_Ehdr* header;

    Elf32_Shdr* sections_headers;
    char*       sections_name;

    Elf32_Shdr* symbols_header;
    Elf32_Sym*  symbols;
    char*       symbols_name;
};

typedef struct Elf_s* Elf_s;

void elf_free(Elf_s elf);

Endianess elf_endianess(Elf_s elf);

// Return the section for the given index
Elf32_Shdr* elf_section_header_at(Elf_s elf, int index);

// Find section header whose name match the argument
// NULL otherwise
Elf32_Shdr* elf_section_find_by_name(Elf_s elf, char* name);

// Common check functions for section
bool elf_section_is_null(Elf32_Shdr* section_header);
bool elf_section_is_symbols(Elf32_Shdr* section_header);
bool elf_section_is_relocations(Elf32_Shdr* section_header);
bool elf_section_is_alloc(Elf32_Shdr* section_header);
bool elf_section_is_nobits(Elf32_Shdr* section_header);

// Some section acts as table, this function returns their
// number of entries
int elf_section_entries_number(Elf32_Shdr* section_header);

// Get the name of the given section
char* elf_section_name(Elf_s elf, Elf32_Shdr* section_header);

// Find the symbol whose name match the argument
// NULL otherwise
Elf32_Sym* elf_symbol_find_by_name(Elf_s elf, char* name);

// Find the section header for the symbol table
Elf32_Shdr* elf_symbol_find_header(Elf_s elf);

// Get the name of the given symbol
char* elf_symbol_name(Elf_s elf, Elf32_Sym* symbol);

#endif
