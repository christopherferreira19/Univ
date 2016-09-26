#include <stdlib.h>
#include <stdint.h>
#include "msg.h"
#include "endianess.h"
#include "elf_write.h"

bool write_at(Elf_s elf, void* ptr, size_t size, uint32_t file_pos);
bool write_header(Elf_s elf);
bool write_section_header(Elf_s elf, Elf32_Shdr* section_header);
bool write_sections_headers(Elf_s elf);
bool write_symbol(Elf_s elf, Elf32_Sym* symbol);
bool write_symbols_section(Elf_s elf);
bool write_raw_section(Elf_s elf, Elf32_Shdr* section_header, uint8_t* section);
bool copy_raw_section(Elf_s src, Elf32_Shdr* section_header_src, Elf_s dst, Elf32_Shdr* section_header_dst);

bool elf_write(Elf_s src, Elf_s dst, char* filename_dst, int* sections_mapping) {
    dst->file = fopen(filename_dst, "w+b");
    if (dst->file == NULL) {
        err("Impossible de créer le fichier résultat\n");
        return false;
    }

    bool ok = true;

    ok = ok && write_header(dst);
    ok = ok && write_sections_headers(dst);
    ok = ok && write_symbols_section(dst);
    Elf32_Shdr* sections_name_header = elf_section_header_at(dst, dst->header->e_shstrndx);
    ok = ok && write_raw_section(dst, sections_name_header, (uint8_t*) dst->sections_name);
    Elf32_Shdr* symbols_name_header = elf_section_header_at(dst, dst->symbols_header->sh_link);
    ok = ok && write_raw_section(dst, symbols_name_header,  (uint8_t*) dst->symbols_name);

    Elf32_Shdr* section_header_src = src->sections_headers;
    Elf32_Shdr* section_header_dst = dst->sections_headers;
    for (int i = 0; i < src->header->e_shnum; i++, section_header_src++) {
        if (elf_section_is_symbols(section_header_dst)
                || elf_section_is_nobits(section_header_dst)
                || sections_mapping[i] == dst->header->e_shstrndx
                || sections_mapping[i] == dst->symbols_header->sh_link) {
            section_header_dst++;
            continue;
        }

        if (sections_mapping[i] != -1) {
            ok = ok && copy_raw_section(src, section_header_src, dst, section_header_dst);
            section_header_dst++;
        }
    }

    return ok;
}

bool elf_write_at(Elf_s elf, void* ptr, size_t size, uint32_t file_pos) {
    fseek(elf->file, file_pos, SEEK_SET);
    return endianess_aware_write(elf->file,
            elf_endianess(elf),
            ptr,
            size);
}

#define fwrite_field(field) \
    if (!endianess_aware_write(elf->file, \
            elf_endianess(elf), \
            &(field), sizeof(field))) { \
        return false; \
    }

bool write_header(Elf_s elf) {
    Elf32_Ehdr* header = elf->header;
    if (fwrite(header->e_ident, sizeof(char), EI_NIDENT, elf->file) != EI_NIDENT) {
        return false;
    }

    fwrite_field(header->e_type);
    fwrite_field(header->e_machine);
    fwrite_field(header->e_version);
    fwrite_field(header->e_entry);
    fwrite_field(header->e_phoff);
    fwrite_field(header->e_shoff);
    fwrite_field(header->e_flags);
    fwrite_field(header->e_ehsize);
    fwrite_field(header->e_phentsize);
    fwrite_field(header->e_phnum);
    fwrite_field(header->e_shentsize);
    fwrite_field(header->e_shnum);
    fwrite_field(header->e_shstrndx);

    return true;
}

bool write_section_header(Elf_s elf, Elf32_Shdr* section_header) {
    fwrite_field(section_header->sh_name);
    fwrite_field(section_header->sh_type);
    fwrite_field(section_header->sh_flags);
    fwrite_field(section_header->sh_addr);
    fwrite_field(section_header->sh_offset);
    fwrite_field(section_header->sh_size);
    fwrite_field(section_header->sh_link);
    fwrite_field(section_header->sh_info);
    fwrite_field(section_header->sh_addralign);
    fwrite_field(section_header->sh_entsize);
    return true;
}

bool write_sections_headers(Elf_s elf) {
    int offset = elf->header->e_shoff;
    int entsize = elf->header->e_shentsize;
    int number = elf->header->e_shnum;

    Elf32_Shdr* section_header = elf->sections_headers;
    for (int i = 0; i < number; i++) {
        if (fseek(elf->file, offset + entsize * i, SEEK_SET)) {
            return false;
        }

        if (!write_section_header(elf, section_header)) {
            return false;
        }

        section_header++;
    }

    return true;
}

bool write_symbol(Elf_s elf, Elf32_Sym* symbol) {
    fwrite_field(symbol->st_name);
    fwrite_field(symbol->st_value);
    fwrite_field(symbol->st_size);
    fwrite_field(symbol->st_info);
    fwrite_field(symbol->st_other);
    fwrite_field(symbol->st_shndx);
    return true;
}

bool write_symbols_section(Elf_s elf) {
    int offset = elf->symbols_header->sh_offset;
    int entsize = elf->symbols_header->sh_entsize;
    int number = elf_section_entries_number(elf->symbols_header);

    Elf32_Sym* symbol = elf->symbols;
    for (int i = 0; i < number; i++) {
        if (fseek(elf->file, offset + entsize * i, SEEK_SET)) {
            return false;
        }

        if (!write_symbol(elf, symbol)) {
            return false;
        }

        symbol++;
    }

   return true;
}

bool write_raw_section(Elf_s elf, Elf32_Shdr* section_header, uint8_t* section) {
    if (fseek(elf->file, section_header->sh_offset, SEEK_SET)) {
        return false;
    }

    int size = section_header->sh_size;
    return fwrite(section, sizeof(uint8_t), size, elf->file) == size;
}

bool copy_raw_section(Elf_s src, Elf32_Shdr* section_header_src, Elf_s dst, Elf32_Shdr* section_header_dst) {
    if (section_header_src->sh_size != section_header_dst->sh_size) {
        return false;
    }
    if (fseek(src->file, section_header_src->sh_offset, SEEK_SET)) {
        return false;
    }
    if (fseek(dst->file, section_header_dst->sh_offset, SEEK_SET)) {
        return false;
    }

    for (int i = 0; i < section_header_src->sh_size; i++) {
        int c = fgetc(src->file);
        if (c == EOF) {
            return false;
        }

        if (fputc((char) c, dst->file) == EOF) {
            return false;
        }
    }

    return true;
}
