#include "msg.h"
#include "elf_read.h"
#include "elf_write.h"
#include "elf_relocate_arm.h"

bool relocate_abs8(Elf_s elf, int32_t file_offset, int32_t s);
bool relocate_abs16(Elf_s elf, int32_t file_offset, int32_t s);
bool relocate_abs32(Elf_s elf, int32_t file_offset, int32_t s);
bool relocate_jump24_call(Elf_s elf, int32_t file_offset, int32_t s, uint32_t p);

bool elf_relocate_arm(Elf_s elf,
        Elf32_Shdr* section_header,
        Elf32_Sym* symbol,
        Elf32_Rel* relocation) {
    int file_offset = section_header->sh_offset + relocation->r_offset;
    int32_t s = symbol->st_value;
    uint32_t p = section_header->sh_addr + relocation->r_offset;

    switch (ELF32_R_TYPE(relocation->r_info)) {
        case R_ARM_ABS32:
            return relocate_abs32(elf, file_offset, s);
        case R_ARM_ABS16:
            return relocate_abs16(elf, file_offset, s);
        case R_ARM_ABS8:
            return relocate_abs8(elf, file_offset, s);
        case R_ARM_CALL:
        case R_ARM_JUMP24:
            return relocate_jump24_call(elf, file_offset, s, p);
        default:
            err("Type inconnu %d,\n", ELF32_R_TYPE(relocation->r_info));
            return false;
    }
}

bool relocate_abs8(Elf_s elf, int32_t file_offset, int32_t s) {
    int8_t addend;

    elf_read_at(elf, &addend, sizeof(addend), file_offset);
    addend = (s + addend) & 0x000000FF;
    elf_write_at(elf, &addend, sizeof(addend), file_offset);
    return true;
}

bool relocate_abs16(Elf_s elf, int32_t file_offset, int32_t s) {
    int16_t addend;

    elf_read_at(elf, &addend, sizeof(addend), file_offset);
    addend = (s + addend) & 0x0000FFFF;
    elf_write_at(elf, &addend, sizeof(addend), file_offset);
    return true;
}

bool relocate_abs32(Elf_s elf, int32_t file_offset, int32_t s) {
    int32_t addend;

    elf_read_at(elf, &addend, sizeof(addend), file_offset);
    addend = s + addend;
    elf_write_at(elf, &addend, sizeof(addend), file_offset);
    return true;
}

int sign_extend(uint32_t value, int sign_index) {
    int mask = 1 << sign_index;
    if (value & mask) {
        do {
            mask <<= 1;
            value |= mask;
        } while (mask > 0);
    }

    return value;
}

bool relocate_jump24_call(Elf_s elf, int32_t file_offset, int32_t s, uint32_t p) {
    s >>= 2;
    p >>= 2;

    int32_t word;
    elf_read_at(elf, &word, sizeof(word), file_offset);
    int32_t addend = sign_extend(word & 0x00FFFFFF, 23);

    int32_t value = s + addend - p;
    value = (word & 0xFF000000) | (value & 0x00FFFFFF);
    elf_write_at(elf, &value, sizeof(value), file_offset);
    return true;
}

