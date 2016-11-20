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

    for (int i = 1; i < 256; i++) {
        histo[i] += histo[i - 1];
    }

    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);

    int total_pixels = src->cols * src->rows;
    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            PGM_AT(dest, i, j) = src->maxval * histo[PGM_AT(src, i, j)] / total_pixels;
        }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}