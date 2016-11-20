#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
      printf("\nUsage: %s file \n\n", argv[0]);
      exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);

    int histo[256] = {0, };

    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            histo[PGM_AT(src, i, j)]++;
        }
    }

    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);

    int minval = 0;
    while (histo[minval] == 0) minval++;
    int maxval = 255;
    while (histo[maxval] == 0) maxval--;

    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            PGM_AT(dest, i, j) = src->maxval * (PGM_AT(src, i, j) - minval) / (maxval - minval);
        }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}