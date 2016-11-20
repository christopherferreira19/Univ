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
    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);
    
    for (int i = 1; i < dest->rows - 1; i++) {
      for (int j = 1; j < dest->cols - 1; j++) {
          gray g = PGM_AT(src, i, j);

          gray c1 = PGM_AT(src, i-1, j-1);
          gray c2 = PGM_AT(src, i-1, j+1);
          gray c3 = PGM_AT(src, i+1, j-1);
          gray c4 = PGM_AT(src, i+1, j+1);

          gray s1 = PGM_AT(src, i-1, j);
          gray s2 = PGM_AT(src, i, j-1);
          gray s3 = PGM_AT(src, i+1, j);
          gray s4 = PGM_AT(src, i, j+1);

          PGM_AT(dest, i, j) = (
                  c1 + c2 + c3 + c4
                  + 2 * s1 + 2 * s2 + 2 * s3 + 2 * s4
                  + 4 * g) / 16;
      }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}

