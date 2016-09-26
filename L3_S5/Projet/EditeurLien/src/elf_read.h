#ifndef __ELF_READ__
#define __ELF_READ__

#include "elf_common.h"

// Construct the Elf_s struct reading data
// from the given file
Elf_s elf_read(char* filename);

// Read the given 'section_header''s content as a bytes array
// Returns NULL if section is of type SHT_NOBITS
uint8_t* elf_read_raw_section(Elf_s s, Elf32_Shdr* section_header);

// Read the relocations content for the given 'section_header'
// Return null if not a relocation table
Elf32_Rel* elf_read_relocations(Elf_s elf, Elf32_Shdr* section_header);

// Read data of given 'size' in 'ptr' starting at 'file_pos'
// in the file associated to 'elf' while respecting endianess
bool elf_read_at(Elf_s elf, void* ptr, size_t size, uint32_t file_pos);

#endif
