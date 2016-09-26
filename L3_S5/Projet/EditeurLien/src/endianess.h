#ifndef __ENDIANESS_AWARE_IO__
#define __ENDIANESS_AWARE_IO__

#include <stdint.h>
#include <stdbool.h>
#include <stdio.h>

typedef enum {
    BIG_ENDIAN    = 0,
    LITTLE_ENDIAN = 1
} Endianess;

// Change endianess according to parameter 'little_endian' and platform endianess
void force_endianess(void* ptr, size_t size, Endianess endianess);

// Read data of given 'size' in 'ptr' starting in 'file'
// while respecting the executing platform endianess
bool endianess_aware_read(FILE* file, Endianess file_endianess, void *ptr, size_t size);

// Write data of given 'size' in 'ptr' starting to 'file'
// while respecting the executing platform endianess
bool endianess_aware_write(FILE* file, Endianess file_endianess, void *ptr, size_t size);

#endif