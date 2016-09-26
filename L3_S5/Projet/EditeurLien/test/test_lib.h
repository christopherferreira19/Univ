#ifndef __TEST_LIB_H__
#define __TEST_LIB_H__

#include "elf_common.h"
#include "elf_read.h"

//GLOBAL
//================
int asserts_run;
int tests_run;

//CONST
//================
#define ID_SIZE 5

//ASSERTS
//================
#define mu_assert(message, test) do { \
    asserts_run++; \
    if (!(test)) { \
        return message; \
    } \
} while (0)

#define mu_run_test(test) do { \
    char *message = test(); \
    tests_run++; \
    if (message) return message; \
} while (0)

#define mu_run_tests(test) do { \
    char *message = test; \
    if (message) return message; \
} while (0)

#define mu_run_tests_file(test, file) do { \
    char *message = test(file); \
    tests_run++; \
    if (message) return message; \
} while (0)

//UTILS
//================
char* r_concat (char* arg, ...);

#define concat(...) r_concat(__VA_ARGS__, "\0");

#define to_str(str, int_id) char str[ID_SIZE]; do { \
        sprintf(str, "%d", int_id); } while (0);


//FUNC
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
        Elf32_Word sh_entsize);

char* assert_symbols (
        int symbol_id,
        Elf_s elf,
        Elf32_Addr st_value,
        Elf32_Word st_size,
        unsigned char st_bind,
        unsigned char st_info,
        unsigned char st_other,
        Elf32_Half st_shndx);

char* assert_relocation (
        char* section_id,
        int relocation_id,
        Elf32_Rel* relocations,
        Elf32_Addr r_offset,
        Elf32_Word r_info
        );

char* assert_relocation_header (
    	char* section_id,
    	Elf32_Shdr* section_header,
    	Elf32_Word sh_link,
    	Elf32_Word sh_info);

#endif
