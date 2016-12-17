#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

pgm_t* sobel_x(pgm_t* src) {
    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);
    for (int i = 1; i < dest->rows - 1; i++) {
        for (int j = 1; j < dest->cols - 1; j++) {
            gray v1 = -PGM_AT(src, i-1, j-1);
            gray v2 = -PGM_AT(src, i  , j-1) * 2;
            gray v3 = -PGM_AT(src, i+1, j-1);
            gray v4 =  PGM_AT(src, i-1, j+1);
            gray v5 =  PGM_AT(src, i  , j+1) * 2;
            gray v6 =  PGM_AT(src, i+1, j+1);

            PGM_AT(dest, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return dest;
}

pgm_t* sobel_y(pgm_t* src) {
    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);
    for (int i = 1; i < dest->rows - 1; i++) {
        for (int j = 1; j < dest->cols - 1; j++) {
            gray v1 = -PGM_AT(src, i-1, j-1);
            gray v2 = -PGM_AT(src, i-1, j  ) * 2;
            gray v3 = -PGM_AT(src, i-1, j+1);
            gray v4 =  PGM_AT(src, i+1, j-1);
            gray v5 =  PGM_AT(src, i+1, j  ) * 2;
            gray v6 =  PGM_AT(src, i+1, j+1);

            PGM_AT(dest, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return dest;
}


int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
        printf("\nUsage: %s <file>\n\n", argv[0]);
        exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);
    
    pgm_t* sobx = sobel_x(src);
    pgm_t* soby = sobel_y(src);

    pgm_write_filename(sobx, BINARY, argv[1], "sobel_x");
    pgm_write_filename(soby, BINARY, argv[1], "sobel_y");

    pgm_free(src);
    pgm_free(sobx);
    pgm_free(soby);

    return 0;
}
