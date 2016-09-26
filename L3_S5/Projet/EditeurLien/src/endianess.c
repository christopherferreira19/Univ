#include "endianess.h"

static uint32_t endianess_test = 1;

static bool platform_endianess() {
    return *((uint8_t*) &endianess_test);
}

static bool platform_endianess_match(Endianess endianess) {
    return platform_endianess() == endianess;
}

void force_endianess(void* ptr, size_t size, Endianess endianess) {
    if (platform_endianess_match(endianess)) {
        return;
    }

    uint8_t* ptr_start = (uint8_t*) ptr;
    uint8_t* ptr_end = ptr_start + size - 1;

    while (ptr_start < ptr_end) {
        uint8_t tmp = *ptr_start;
        *ptr_start = *ptr_end;
        *ptr_end = tmp;

        ptr_start++;
        ptr_end--;
    }
}

bool endianess_aware_read(FILE* file, Endianess file_endianess, void *ptr, size_t size) {
    if (platform_endianess_match(file_endianess)) {
        return fread(ptr, size, 1, file);
    }
    else {
        uint8_t* ptr_c1 = (uint8_t*) ptr;
        uint8_t* ptr_c2 = ptr_c1 + size - 1;

        while (ptr_c2 >= ptr_c1) {
            *ptr_c2 = fgetc(file);
            ptr_c2--;

            if (feof(file) || ferror(file)) {
                return 0;
            }
        }

        return !ferror(file);
    }
}

bool endianess_aware_write(FILE* file, Endianess file_endianess, void *ptr, size_t size) {
    if (platform_endianess_match(file_endianess)) {
        return fwrite(ptr, size, 1, file);
    }
    else {
        uint8_t* ptr_c1 = (uint8_t*) ptr;
        uint8_t* ptr_c2 = ptr_c1 + size - 1;

        while (ptr_c2 >= ptr_c1) {
            fputc(*ptr_c2, file);
            ptr_c2--;

            if (feof(file) || ferror(file)) {
                return false;
            }
        }

        return !ferror(file);
    }
}
