#include <stdlib.h>
#include <string.h>
#include "elf_common.h"

void elf_free(Elf_s elf) {
    free(elf->symbols_name);
    free(elf->symbols);
    free(elf->sections_name);
    free(elf->sections_headers);
    if (elf->file != NULL) {
        fclose(elf->file);
    }
    free(elf->header);
    free(elf);
}

Endianess elf_endianess(Elf_s elf) {
    return elf->header->e_ident[EI_DATA] == ELFDATA2LSB;
}

Elf32_Shdr* elf_section_header_at(Elf_s elf, int index) {
    return elf->sections_headers + index;
}


Elf32_Shdr* elf_section_find_by_name(Elf_s elf, char* name) {
    int num_of_sections = elf->header->e_shnum;
    Elf32_Shdr* current = elf->sections_headers;

    for (int i = 0; i < num_of_sections; i++, current++) {
        if (!strcmp(elf_section_name(elf,current),name))
            return current;
    }

    return NULL;
}

bool elf_section_is_null(Elf32_Shdr* section_header) {
    return section_header->sh_type == SHT_NULL;
}

bool elf_section_is_symbols(Elf32_Shdr* section_header) {
    return section_header->sh_type == SHT_SYMTAB;
}

bool elf_section_is_relocations(Elf32_Shdr* section_header) {
    return section_header->sh_type == SHT_REL;
}

bool elf_section_is_alloc(Elf32_Shdr* section_header) {
    return section_header->sh_flags & SHF_ALLOC;
}

bool elf_section_is_nobits(Elf32_Shdr* section_header) {
    return section_header->sh_type == SHT_NOBITS;
}

int elf_section_entries_number(Elf32_Shdr* section_header) {
    return section_header->sh_size / section_header->sh_entsize;
}

char* elf_section_name(Elf_s elf, Elf32_Shdr* section_header) {
    return elf->sections_name + section_header->sh_name;
}

Elf32_Shdr* elf_symbol_find_header(Elf_s elf) {
    Elf32_Shdr* section = elf->sections_headers;

    for (int i = 0; i < elf->header->e_shnum; i++) {
        if (section->sh_type == SHT_SYMTAB) {
            return section;
        }

        section++;
    }

    return NULL;
}

Elf32_Sym* elf_symbol_find_by_name(Elf_s elf, char* name) {
    Elf32_Sym *current = elf->symbols;
    int number_of_symbols = elf->symbols_header->sh_size / elf->symbols_header->sh_entsize;

    for(int i = 0;i< number_of_symbols; i++, current++) {
        if (!strcmp(elf_symbol_name(elf,current),name)){
            return current;
            break;
        }
    }
    return NULL;
}

char* elf_symbol_name(Elf_s elf, Elf32_Sym* symbol) {
    return elf->symbols_name + symbol->st_name;
}
