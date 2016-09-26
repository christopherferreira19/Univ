#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <elf.h>
#include "msg.h"
#include "elf_common.h"
#include "elf_read.h"
#include "elf_print.h"

int main (int argc, char * argv[]) {
    init_msg(WARNING);

    if (argc != 2) {
        printf("Usage: %s <file> \n", argv[0]);
        return 1;
    }

    Elf_s elf = elf_read(argv[1]);
    if (elf == NULL) {
        return 1;
    }
    else {
        elf_print(elf);
        elf_free(elf);

        return 0;
    }
}
