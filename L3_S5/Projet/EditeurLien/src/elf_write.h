#ifndef __ELF_WRITE_H__
#define __ELF_WRITE_H__

#include <stdio.h>
#include <stdbool.h>
#include <elf.h>
#include "elf_common.h"

// Write the data of the 'dst' ELF data copying sections
// content from the 'src' ELF.
// sections_mapping gives the mapping between 'src' and
// 'dst' sections indexes
bool elf_write(Elf_s src, Elf_s dst, char* filename, int* sections_mapping);

// Write data of given 'size' in 'ptr' starting at 'file_pos'
// in the file associated to 'elf' while respecting endianess
bool elf_write_at(Elf_s elf, void* ptr, size_t size, uint32_t file_pos);

#endif
