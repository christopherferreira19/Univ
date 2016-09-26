#include "elf_common.h"
#include "elf_relocate.h"
#include "elf_print.h"
#include "test_lib.h"
#include "test_relocate.h"
#include "elf_rel_args.h"

Elf_s relocate(char* filename,
        int section_addr_size,
        Arg_Addr* section_addrs,
        char* entry_point);

char* test_relocate_invalid() {
	Elf_s elf = elf_relocate(
		"test/files/relocate_basic", // ELF de type EXEC, non relogeable
		"build/relocate_basic",
		0,
		NULL,
		"main");

	mu_assert("Pas de relocation pour un fichier EXEC", elf == NULL);

	return 0;
}

char* test_relocate_overlap1() {
    int section_addr_size = 3;
    Arg_Addr section_addrs[section_addr_size];
    section_addrs[0].section = ".text";
    section_addrs[0].addr = 0x200;
    section_addrs[1].section = ".data";
    section_addrs[1].addr = 0x107;
    section_addrs[2].section = ".bss";
    section_addrs[2].addr = 0x100;

    Elf_s elf = elf_relocate(
        "test/files/relocate_3_sections", // ELF de type EXEC, non relogeable
        "build/relocate_3_sections",
        section_addr_size,
        section_addrs,
        "main");
    mu_assert("Overlap not recognised 1", elf == NULL);

    return 0;
}

char* test_relocate_overlap2() {
    int section_addr_size = 3;
    Arg_Addr section_addrs[section_addr_size];
    section_addrs[0].section = ".text";
    section_addrs[0].addr = 0x100;
    section_addrs[1].section = ".data";
    section_addrs[1].addr = 0x107;
    section_addrs[2].section = ".bss";
    section_addrs[2].addr = 0x200;

    Elf_s elf = elf_relocate(
        "test/files/relocate_3_sections", // ELF de type EXEC, non relogeable
        "build/relocate_3_sections",
        section_addr_size,
        section_addrs,
        "main");
    mu_assert("Overlap not recognised 2", elf == NULL);

    return 0;
}

char* test_relocate_basic() {
	int section_addr_size = 1;
	Arg_Addr section_addrs[section_addr_size];
	section_addrs[0].section = ".text";
	section_addrs[0].addr = 0x20;

	Elf_s elf = relocate("relocate_basic",
		section_addr_size,
		section_addrs,
		"main");
	mu_assert("Lecture relocate_basic", elf != NULL);

	mu_assert("Mise à jour du type en EXEC", elf->header->e_type == ET_EXEC);
	mu_assert("Mise à jour du point d'entrée", elf->header->e_entry == 0x20);
	mu_assert("Mise à jour du flag de point d'entrée", elf->header->e_flags & SHF_ENTRY_POINT);

    elf_free(elf);

	return 0;
}

char* test_relocate_3_sections() {
	int section_addr_size = 3;
	Arg_Addr section_addrs[section_addr_size];
	section_addrs[0].section = ".text";
	section_addrs[0].addr = 0x100;
	section_addrs[1].section = ".data";
	section_addrs[1].addr = 0x200;
	section_addrs[2].section = ".bss";
	section_addrs[2].addr = 0x300;

	Elf_s elf = relocate("relocate_3_sections",
		section_addr_size,
		section_addrs,
		"main");
	mu_assert("Lecture relocate_3_sections", elf != NULL);

	Elf32_Shdr* text_section = elf_section_find_by_name(elf, ".text");
	Elf32_Shdr* data_section = elf_section_find_by_name(elf, ".data");
	Elf32_Shdr*  bss_section = elf_section_find_by_name(elf, ".bss");

	mu_assert(".text@0x100", text_section->sh_addr == 0x100);
	mu_assert(".data@0x200", data_section->sh_addr == 0x200);
	mu_assert(" .bss@0x300",  bss_section->sh_addr == 0x300);

    elf_free(elf);

	return 0;
}

char* test_relocate_alignment() {
	int section_addr_size = 1;
	Arg_Addr section_addrs[section_addr_size];
	section_addrs[0].section = ".text";
	section_addrs[0].addr = 0x10;

	Elf_s elf = relocate("relocate_alignment",
		section_addr_size,
		section_addrs,
		"main");
	mu_assert("Lecture relocate_alignment", elf != NULL);

	Elf32_Shdr* text_section = elf_section_find_by_name(elf, ".text");
	Elf32_Shdr* data_section = elf_section_find_by_name(elf, ".data");
	Elf32_Shdr*  bss_section = elf_section_find_by_name(elf, ".bss");

	mu_assert("Position de .text: 0x10", text_section->sh_addr == 0x10);
	mu_assert("Taille de .text : 0x03", text_section->sh_size == 0x02);
	mu_assert("Position de .data: 0x12", data_section->sh_addr == 0x12);
	mu_assert("Taille de .data : 0x01", data_section->sh_size == 0x01);
	mu_assert("Alignement de .bss respecté", bss_section->sh_addr == 0x18);

    elf_free(elf);

	return 0;
}

char* test_relocate_abs() {
    int section_addr_size = 2;
    Arg_Addr section_addrs[section_addr_size];
    section_addrs[0].section = ".text";
    section_addrs[0].addr = 0x10;
    section_addrs[1].section =".data";
    section_addrs[1].addr = 0x30;

    Elf_s elf = relocate("relocate_abs",
        section_addr_size,
        section_addrs,
        "main");
    mu_assert("Lecture relocate_abs", elf != NULL);

    Elf32_Shdr* text_section = elf_section_find_by_name(elf, ".text");
    Elf32_Shdr* data_section = elf_section_find_by_name(elf, ".data");

    mu_assert("Position de .text: 0x10", text_section->sh_addr == 0x10);
    mu_assert("Position de .data: 0x30", data_section->sh_addr == 0x30);

    uint8_t* text = (uint8_t*) elf_read_raw_section(elf, text_section);
    mu_assert("Relocate ASB32[0]", text[0x10] == 0x00);
    mu_assert("Relocate ASB32[1]", text[0x11] == 0x00);
    mu_assert("Relocate ASB32[2]", text[0x12] == 0x00);
    mu_assert("Relocate ASB32[3]", text[0x13] == 0x30);
    mu_assert("Relocate ASB16[0]", text[0x14] == 0x00);
    mu_assert("Relocate ASB16[1]", text[0x15] == 0x30);
    mu_assert("Relocate ASB8",     text[0x16] == 0x30);

    free(text);
    elf_free(elf);

    return 0;
}

char* test_relocate_jump24_call() {
    int section_addr_size = 2;
    Arg_Addr section_addrs[section_addr_size];
    section_addrs[0].section = ".text";
    section_addrs[0].addr = 0x20;
    section_addrs[1].section =".data";
    section_addrs[1].addr = 0x100;

    Elf_s elf = relocate("relocate_jump24_call",
        section_addr_size,
        section_addrs,
        "main");
    mu_assert("Lecture relocate_jump24_call", elf != NULL);

    Elf32_Shdr* text_section = elf_section_find_by_name(elf, ".text");
    Elf32_Shdr* data_section = elf_section_find_by_name(elf, ".data");

    mu_assert("Position de .text: 0x20", text_section->sh_addr == 0x20);
    mu_assert("Position de .data: 0x100", data_section->sh_addr == 0x100);

    uint32_t* text = (uint32_t*) elf_read_raw_section(elf, text_section);
    mu_assert("Relocate jump24 call 0x04", text[0x04 >> 2] == 0x370000eb);
    mu_assert("Relocate jump24 call 0x08", text[0x08 >> 2] == 0x3a0000eb);
    mu_assert("Relocate jump24 call 0x0c", text[0x0c >> 2] == 0x350000ea);
    mu_assert("Relocate jump24 call 0x10", text[0x10 >> 2] == 0x380000ea);
    mu_assert("Relocate jump24 call 0x18", text[0x18 >> 2] == 0xf9ffffeb);
    mu_assert("Relocate jump24 call 0x20", text[0x20 >> 2] == 0xf7ffffea);

    free(text);
    elf_free(elf);

    return 0;
}

Elf_s relocate(char* filename,
        int section_addr_size,
        Arg_Addr* section_addrs,
        char* entry_point) {
    char* filename_src = concat("test/files/", filename, ".o");
    char* filename_dst = concat("build/", filename);

    Elf_s elf = elf_relocate(
        filename_src,
        filename_dst,
        section_addr_size,
        section_addrs,
        entry_point);
    if (elf == NULL) {
        return NULL;
    }

    elf_free(elf);
    // On relit entièrement le fichier par précaution
    return elf_read(filename_dst);
}
