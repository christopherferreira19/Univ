#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include "test_lib.h"

//libelf
#include <err.h>
#include <fcntl.h>
#include <libelf.h>
#include <unistd.h>
#include <gelf.h>


#include "test_read.h"

void free_end_close(Elf_s elf, Elf* e, int fd) {
    elf_free(elf); \
    (void) elf_end(e); \
    (void) close(fd);
}

void libelf_version() {
    if (elf_version(EV_CURRENT) == EV_NONE) {
        errx(EXIT_FAILURE, "ELF library initialization "
        "failed: %s", elf_errmsg(-1));
    }
}

//HARDCODE_TEST
//================
char* test_example1_relocation_section() {
    Elf_s elf = elf_read("test/files/example1.o");
    mu_assert("Lecture", elf != NULL);
    char section_id[ID_SIZE];
    for (int i = 0; i < elf->header->e_shnum; i++) {
        Elf32_Shdr* section_header = elf_section_header_at(elf, i);
        if (!elf_section_is_relocations(section_header)) {
            continue;
        }
        sprintf(section_id, "%d", i);

        Elf32_Rel* relocations = elf_read_relocations(elf, section_header);
        mu_assert(concat("test_relocation_section : ouverture relocations", section_id), relocations != NULL);

        mu_assert(concat("test_relocation_section : ouverture section_header", section_id), relocations != NULL);

        //assert_relocation_header(section_id, section_header, sh_link_test, sh_info_test);
        //assert_relocation(section_id, j, relocations[i], r_offset, r_info);
        switch(i) {
            case 6  :
                 mu_run_tests(assert_relocation_header(section_id, section_header, 13, 5));
                 mu_run_tests(assert_relocation(section_id, 0, relocations, 0x2E, 0x102));
            break;

            case 8  :
                 mu_run_tests(assert_relocation_header(section_id, section_header, 13, 7));
                 mu_run_tests(assert_relocation(section_id, 0, relocations, 0x6, 0x902));
                 mu_run_tests(assert_relocation(section_id, 1, relocations, 0xC, 0xA02));
                 mu_run_tests(assert_relocation(section_id, 2, relocations, 0x10, 0x102));
                 mu_run_tests(assert_relocation(section_id, 3, relocations, 0x14, 0x102));
            break;

            case 11  :
                 mu_run_tests(assert_relocation_header(section_id, section_header, 13, 10));
                 mu_run_tests(assert_relocation(section_id, 0, relocations, 0x6, 0x702));
                 mu_run_tests(assert_relocation(section_id, 1, relocations, 0x10, 0x102));
            break;

            default :
                return "invalid test_relocation_section input in test.c";
        }

        free(relocations);
    }

    elf_free(elf);

    return 0;
}



Elf* libelf_read(int * fd_result, char * filename) {
    Elf * e;
    int fd;
    if ((fd = open(filename, O_RDONLY, 0)) < 0)
        err(EXIT_FAILURE, "open \"%s\" failed", filename);

    if ((e = elf_begin(fd, ELF_C_READ, NULL)) == NULL)
        errx(EXIT_FAILURE, "elf_begin() failed: %s.",
            elf_errmsg(-1));

    if (elf_kind(e) != ELF_K_ELF)
        errx(EXIT_FAILURE, "%s is not an ELF object.",
            filename);

    *fd_result = fd;
    return e;
}

char* test_libelf_relocate(char* filename) {
    int fd;
    GElf_Shdr shdr;
    Elf_Scn *scn = NULL;
    Elf_Data *edata = NULL;
    Elf32_Rel* relocations;
    char* result;

    Elf_s elf = elf_read(filename);
    mu_assert("Lecture", elf != NULL);

    libelf_version();

    Elf* e = libelf_read(&fd, filename);

    int section_id = 1;
    char section_id_str[ID_SIZE];

    while ((scn = elf_nextscn(e, scn)) != NULL) {
            gelf_getshdr(scn, &shdr);

        if (shdr.sh_type == SHT_REL) {
            edata = elf_getdata(scn, edata);

            sprintf(section_id_str, "%d", section_id);
            
            Elf32_Shdr* section_header = elf_section_header_at(elf, section_id);
            relocations = elf_read_relocations(elf, section_header);
            mu_assert("read relocation", relocations != NULL);

            result = assert_relocation_header(section_id_str, section_header, shdr.sh_link, shdr.sh_info);
            if (result != 0) return result;
        }
        
    section_id++;
    }

    free_end_close(elf, e, fd);
    return 0;
}

char* test_libelf_symbols(char* filename) {
    int fd, symbol_count;
    GElf_Shdr shdr;
    GElf_Sym sym;
    Elf_Scn *scn = NULL;
    Elf_Data *edata = NULL;
    char* result;

    Elf_s elf = elf_read(filename);
    mu_assert("Lecture", elf != NULL);

    libelf_version();

    Elf* e = libelf_read(&fd, filename);

    while ((scn = elf_nextscn(e, scn)) != NULL) {
            gelf_getshdr(scn, &shdr); 

        if (shdr.sh_type == SHT_SYMTAB) {
            edata = elf_getdata(scn, edata);
            symbol_count = shdr.sh_size / shdr.sh_entsize;

            for (int i = 0; i < symbol_count; i++) {
                gelf_getsym(edata, i, &sym);

                result = assert_symbols (
                    i,
                    elf,
                    sym.st_value,
                    sym.st_size,
                    ELF32_ST_BIND(sym.st_info),
                    ELF32_ST_TYPE(sym.st_info),
                    sym.st_other,
                    sym.st_shndx);

                if (result != 0) {
                    free_end_close(elf, e, fd);
                    return result;
                }
            }
        }
    }

    free_end_close(elf, e, fd);
    return 0;
}

char* test_libelf_section(char* filename) {

    int fd;
    char *name;
    Elf_Scn *scn = NULL;
    GElf_Shdr shdr;
    size_t shstrndx;
    char* result;

    libelf_version();

    Elf_s elf = elf_read(filename);
    Elf* e = libelf_read(&fd, filename);

    if (elf_getshdrstrndx(e, &shstrndx) != 0)
        errx(EXIT_FAILURE, "elf_getshdrstrndx() failed: %s.",
            elf_errmsg(-1));

    while ((scn = elf_nextscn(e, scn)) != NULL) {
        if (gelf_getshdr(scn, &shdr) != &shdr)
            errx(EXIT_FAILURE, "getshdr() failed: %s.",
                elf_errmsg(-1));

        if ((name = elf_strptr(e, shstrndx, shdr.sh_name))
            == NULL)
            errx(EXIT_FAILURE, "elf_strptr() failed: %s.",
                elf_errmsg(-1));

        result = assert_header_section (
        (uintmax_t) elf_ndxscn(scn),
        elf,
        name,
        shdr.sh_type,
        shdr.sh_flags,
        shdr.sh_addr,
        shdr.sh_offset,
        shdr.sh_size,
        shdr.sh_link,
        shdr.sh_info,
        shdr.sh_addralign,
        shdr.sh_entsize);

        if (result != 0) {
            free_end_close(elf, e, fd);
            return result;
        }
    }

    free_end_close(elf, e, fd);
    return 0;
}

GElf_Ehdr libelf_read_header (char* filename) {
    int fd;
    GElf_Ehdr ehdr;

    libelf_version();

    Elf* e = libelf_read(&fd, filename);

    if (gelf_getehdr(e, &ehdr) == NULL)
        errx(EXIT_FAILURE, "getehdr() failed: %s.",
            elf_errmsg(-1));

    (void) elf_end(e);
    (void) close(fd);

    return ehdr;

}

char* test_libelf_header(char* filename) {

    Elf_s elf = elf_read(filename);
    mu_assert("Lecture", elf != NULL);

    GElf_Ehdr ehdr = libelf_read_header(filename);

    #define libelf_header_assert(header_field) do { \
    mu_assert("header_field", elf->header->header_field == ehdr.header_field); } while (0)

    libelf_header_assert(e_ident[0]);
    libelf_header_assert(e_ident[1]);
    libelf_header_assert(e_ident[2]);
    libelf_header_assert(e_ident[3]);
    libelf_header_assert(e_ident[4]);
    libelf_header_assert(e_ident[5]);
    libelf_header_assert(e_machine);
    libelf_header_assert(e_machine);
    libelf_header_assert(e_version);
    libelf_header_assert(e_entry);
    libelf_header_assert(e_phoff);
    libelf_header_assert(e_shoff);
    libelf_header_assert(e_flags);
    libelf_header_assert(e_ehsize);
    libelf_header_assert(e_phentsize);
    libelf_header_assert(e_phnum);
    libelf_header_assert(e_shentsize);
    libelf_header_assert(e_shnum);
    libelf_header_assert(e_shstrndx);

    elf_free(elf);

    return 0;
}
