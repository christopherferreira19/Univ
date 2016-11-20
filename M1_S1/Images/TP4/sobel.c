#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 3 ) {
      printf("\nUsage: %s <file> x|y \n\n", argv[0]);
      exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);
    if (argv[2][1] != '\0' || (argv[2][0] != 'x' && argv[2][0] != 'y')) {
      printf("\nUsage: %s <file> x|y \n\n", argv[0]);
      exit(0);
    }
    bool left_right = argv[2][0] != 'x';

    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);
    for (int i = 1; i < dest->rows - 1; i++) {
      for (int j = 1; j < dest->cols - 1; j++) {
        gray v1, v2, v3, v4, v5, v6;
        if (left_right) {
          v1 = -PGM_AT(src, i-1, j-1);
          v2 = -PGM_AT(src, i-1, j  ) * 2;
          v3 = -PGM_AT(src, i-1, j+1);
          v4 =  PGM_AT(src, i+1, j-1);
          v5 =  PGM_AT(src, i+1, j  ) * 2;
          v6 =  PGM_AT(src, i+1, j+1);
        }
        else {
          v1 = -PGM_AT(src, i-1, j-1);
          v2 = -PGM_AT(src, i  , j-1) * 2;
          v3 = -PGM_AT(src, i+1, j-1);
          v4 =  PGM_AT(src, i-1, j+1);
          v5 =  PGM_AT(src, i  , j+1) * 2;
          v6 =  PGM_AT(src, i+1, j+1);
        }

        PGM_AT(dest, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
      }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}
