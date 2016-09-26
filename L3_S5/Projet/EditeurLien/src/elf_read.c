#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include "msg.h"
#include "endianess.h"
#include "elf_read.h"

bool read_header_fields(Elf_s elf);
bool read_header(Elf_s elf);
bool read_section_header(Elf_s elf, Elf32_Shdr* section_header);
bool read_sections_headers(Elf_s elf);
bool read_symbol(Elf_s elf, Elf32_Sym* symbol);
bool read_symbols(Elf_s elf);
bool read_relocation(Elf_s elf, Elf32_Rel* rel);

Elf_s elf_read(char* filename) {
    FILE* f = fopen(filename, "rb");
    if (f == NULL) {
        err("Unable to read file %s\n", filename);
        return NULL;
    }

    Elf_s elf = malloc(sizeof(struct Elf_s));
    if (elf == NULL) {
        return NULL;
    }

    elf->file = f;
    elf->header = NULL;
    elf->sections_headers = NULL;
    elf->sections_name = NULL;
    elf->symbols_header = NULL;
    elf->symbols = NULL;
    elf->symbols_name = NULL;

    read_header(elf);

    // Lecture header ELF
    if (elf->header == NULL) {
        err("Unable to read ELF file\n");
        elf_free(elf);
        return NULL;
    }
    if (elf->header->e_ident[EI_MAG0] != ELFMAG0
            || elf->header->e_ident[EI_MAG1] != ELFMAG1
            || elf->header->e_ident[EI_MAG2] != ELFMAG2
            || elf->header->e_ident[EI_MAG3] != ELFMAG3) {
        err("File is not ELF (Magic number : %.2X %.2X %.2X %.2X)\n",
                elf->header->e_ident[EI_MAG0],
                elf->header->e_ident[EI_MAG1],
                elf->header->e_ident[EI_MAG2],
                elf->header->e_ident[EI_MAG3]);
        elf_free(elf);
        return NULL;
    }
    if (elf->header->e_ident[EI_CLASS] != ELFCLASS32) {
        err("Only 32 bits ELF file are supported\n");
        elf_free(elf);
        return NULL;
    }

    // Lecture des headers et noms de section
    read_sections_headers(elf);
    if (elf->sections_headers == NULL) {
        err("Unable to read sections headers\n");
        elf_free(elf);
        return NULL;
    }

    Elf32_Shdr* elf_sections_name_header = elf_section_header_at(elf, elf->header->e_shstrndx);
    elf->sections_name = (char*) elf_read_raw_section(elf, elf_sections_name_header);
    if (elf->sections_name == NULL) {
        err("Unable to read sections names\n");
        elf_free(elf);
        return NULL;
    }

    // Lecture des symboles et de leur noms
    elf->symbols_header = elf_symbol_find_header(elf);
    if (elf->symbols_header == NULL) {
        elf->symbols = NULL;
        elf->symbols_name = NULL;
    }
    else {
        read_symbols(elf);
        if (elf->symbols == NULL) {
            err("Unable to read symbols table\n");
            elf_free(elf);
            return NULL;
        }

        Elf32_Shdr* symbols_name_header = elf_section_header_at(elf, elf->symbols_header->sh_link);
        elf->symbols_name = (char*) elf_read_raw_section(elf, symbols_name_header);
        if (elf->symbols_name == NULL) {
            err("Unable to read symbols names\n");
            elf_free(elf);
            return NULL;
        }
    }

    return elf;
}

uint8_t* elf_read_raw_section(Elf_s elf, Elf32_Shdr* section_header) {
    if (elf_section_is_nobits(section_header)) {
        return NULL;
    }
    if (fseek(elf->file, section_header->sh_offset, SEEK_SET)) {
        return NULL;
    }

    int size = section_header->sh_size;
    uint8_t* section = malloc(sizeof(uint8_t) * size);
    if (section == NULL) {
        return NULL;
    }

    if (fread(section, sizeof(uint8_t), size, elf->file) != size) {
        return NULL;
    }

    return section;
}

Elf32_Rel* elf_read_relocations(Elf_s elf, Elf32_Shdr* section_header) {
    if (!elf_section_is_relocations(section_header)) {
        return NULL;
    }

    int offset = section_header->sh_offset;
    int entsize = section_header->sh_entsize;
    int number = elf_section_entries_number(section_header);

    Elf32_Rel* relocations = malloc(number * sizeof(Elf32_Rel));
    if (relocations == NULL) {
        return NULL;
    }

    Elf32_Rel* current = relocations;
    for (int i = 0; i < number; i++) {
        if (fseek(elf->file, offset + entsize * i, SEEK_SET)) {
            free(relocations);
            return NULL;
        }

        if (!read_relocation(elf, current)) {
            free(relocations);
            return NULL;
        }

        current++;
    }

    return relocations;
}

bool elf_read_at(Elf_s elf, void* ptr, size_t size, uint32_t file_pos) {
    fseek(elf->file, file_pos, SEEK_SET);
    return endianess_aware_read(elf->file,
            elf_endianess(elf),
            ptr, size);
}

#define read_field(field) \
    if (!endianess_aware_read(elf->file, \
            elf_endianess(elf), \
            &(field), sizeof(field))) { \
        return false; \
    }

bool read_header_fields(Elf_s elf) {
    read_field(elf->header->e_type);
    read_field(elf->header->e_machine);
    read_field(elf->header->e_version);
    read_field(elf->header->e_entry);
    read_field(elf->header->e_phoff);
    read_field(elf->header->e_shoff);
    read_field(elf->header->e_flags);
    read_field(elf->header->e_ehsize);
    read_field(elf->header->e_phentsize);
    read_field(elf->header->e_phnum);
    read_field(elf->header->e_shentsize);
    read_field(elf->header->e_shnum);
    read_field(elf->header->e_shstrndx);

    return true;
}

bool read_header(Elf_s elf) {
    elf->header = malloc(sizeof(Elf32_Ehdr));
    if (elf->header == NULL) {
        return false;
    }

    fread(elf->header, sizeof(char), EI_NIDENT, elf->file);
    if (feof(elf->file) || ferror(elf->file)) {
        return false;
    }

    if (!read_header_fields(elf)) {
        return false;
    }

    return true;
}

bool read_section_header(Elf_s elf, Elf32_Shdr* section_header) {
    read_field(section_header->sh_name);
    read_field(section_header->sh_type);
    read_field(section_header->sh_flags);
    read_field(section_header->sh_addr);
    read_field(section_header->sh_offset);
    read_field(section_header->sh_size);
    read_field(section_header->sh_link);
    read_field(section_header->sh_info);
    read_field(section_header->sh_addralign);
    read_field(section_header->sh_entsize);
    return true;
}

bool read_sections_headers(Elf_s elf) {
    int offset = elf->header->e_shoff;
    int entsize = elf->header->e_shentsize;
    int number = elf->header->e_shnum;

    elf->sections_headers = malloc(number * sizeof(Elf32_Shdr));
    if (elf->sections_headers == NULL) {
        return false;
    }

    Elf32_Shdr* section_header = elf->sections_headers;
    for (int i = 0; i < number; i++) {
        if (fseek(elf->file, offset + entsize * i, SEEK_SET)) {
            return false;
        }

        if (!read_section_header(elf, section_header)) {
            return false;
        }

        section_header++;
    }

    return true;
}

bool read_symbol(Elf_s elf, Elf32_Sym* symbol) {
    read_field(symbol->st_name);
    read_field(symbol->st_value);
    read_field(symbol->st_size);
    read_field(symbol->st_info);
    read_field(symbol->st_other);
    read_field(symbol->st_shndx);
    return true;
}

bool read_symbols(Elf_s elf) {
    int offset = elf->symbols_header->sh_offset;
    int entsize = elf->symbols_header->sh_entsize;
    int number = elf_section_entries_number(elf->symbols_header);

    elf->symbols = malloc(number * sizeof(Elf32_Sym));
    if (elf->symbols == NULL) {
        return false;
    }

    Elf32_Sym* symbol = elf->symbols;
    for (int i = 0; i < number; i++) {
        if (fseek(elf->file, offset + entsize * i, SEEK_SET)) {
            return false;
        }

        if (!read_symbol(elf, symbol)) {
            return false;
        }

        symbol++;
    }

   return true;
}

bool read_relocation(Elf_s elf, Elf32_Rel* rel) {
    read_field(rel->r_offset);
    read_field(rel->r_info);
    return rel;
}
