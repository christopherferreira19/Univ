#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdarg.h>
#include "test_lib.h"

//GLOBAL
//================
int asserts_run = 0;
int tests_run = 0;

char* base_concat(char* s1, char* s2) {
    size_t len1 = strlen(s1);
    size_t len2 = strlen(s2);
    char* result = malloc(len1+len2+1);
    memcpy(result, s1, len1);
    memcpy(result+len1, s2, len2+1);
    return result;
}

char* r_concat (char* arg, ...){
    if (!strcmp(arg, "\0")) return "r_concat_arg_error\n";
    va_list va;
    va_start (va, arg);

    char* str = arg;
    char* str_c = va_arg (va, char*);

    while (strcmp(str_c, "\0")) {
        str = base_concat(str, str_c);
        str_c = va_arg (va, char*);
    }

    va_end (va);
    return str;
}


//ELF_HEADER
//================
char* assert_header_section (
        int section_id,
        Elf_s elf,
        char* name,
        Elf32_Word sh_type,
        Elf32_Word sh_flags,
        Elf32_Addr sh_addr,
        Elf32_Off sh_offset,
        Elf32_Word sh_size,
        Elf32_Word sh_link,
        Elf32_Word sh_info,
        Elf32_Word sh_addralign,
        Elf32_Word sh_entsize) {

    to_str(id, section_id);

    Elf32_Shdr section_header = elf->sections_headers[section_id];

    mu_assert(concat("name[", id,"]"), !strcmp(elf_section_name(elf, &section_header), name));
    mu_assert(concat("sh_type[", id,"]"), section_header.sh_type == sh_type);
    mu_assert(concat("sh_flags[", id,"]"), section_header.sh_flags == sh_flags);
    mu_assert(concat("sh_addr[", id,"]"), section_header.sh_addr == sh_addr);
    mu_assert(concat("sh_offset[", id,"]"), section_header.sh_offset == sh_offset);
    mu_assert(concat("sh_size[", id,"]"), section_header.sh_size == sh_size);
    mu_assert(concat("sh_link[", id,"]"), section_header.sh_link == sh_link);
    mu_assert(concat("sh_info[", id,"]"), section_header.sh_info == sh_info);
    mu_assert(concat("sh_addralign[", id,"]"), section_header.sh_addralign == sh_addralign);
    mu_assert(concat("sh_entsize[", id,"]"), section_header.sh_entsize == sh_entsize);

    return 0;
}

//SYMBOLS_SECTION
//================
char* assert_symbols (
        int symbol_id,
        Elf_s elf,
        Elf32_Addr st_value,
        Elf32_Word st_size,
        unsigned char st_bind,
        unsigned char st_info,
        unsigned char st_other,
        Elf32_Half st_shndx) {

    to_str(id, symbol_id);

    Elf32_Sym symbol = elf->symbols[symbol_id];

    unsigned char bind = ELF32_ST_BIND(symbol.st_info);
    unsigned char type = ELF32_ST_TYPE(symbol.st_info);

    mu_assert(concat("st_value[", id, "]"), symbol.st_value == st_value);
    mu_assert(concat("st_size[", id, "]"), symbol.st_size == st_size);
    mu_assert(concat("info[", id, "]"), type == st_info);
    mu_assert(concat("bind[", id, "]"), bind == st_bind);
    mu_assert(concat("st_other[", id, "]"), symbol.st_other == st_other);
    mu_assert(concat("st_shndx[", id, "]"), symbol.st_shndx == st_shndx);

    return 0;
}

//RELOCATION
//================
char* assert_relocation (
        char* section_id,
        int relocation_id,
        Elf32_Rel* relocations,
        Elf32_Addr r_offset,
        Elf32_Word r_info) {

    to_str(r_id, relocation_id);
    mu_assert(concat("r_offset[section=", section_id, ", rel=", r_id, "]"), relocations[relocation_id].r_offset == r_offset);
    mu_assert(concat("r_info[section=", section_id, ", rel=", r_id, "]"), relocations[relocation_id].r_info == r_info);

    return 0;
}

char* assert_relocation_header (
        char* section_id,
        Elf32_Shdr* section_header,
        Elf32_Word sh_link,
        Elf32_Word sh_info) {

    mu_assert(concat("sh_link_", section_id), section_header->sh_link == sh_link);
    mu_assert(concat("sh_info_", section_id), section_header->sh_info == sh_info);

    return 0;
}
