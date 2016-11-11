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

    img_t* src = read_image(argv[1]);
    img_t* dest = create_empty_image(src->cols, src->rows, src->maxval);
    
    for (int i = 1; i < src->rows - 1; i++) {
      for (int j = 1; j < src->cols - 1; j++) {
          gray g = IMG_AT(src, i, j);

          gray c1 = IMG_AT(src, i-1, j-1);
          gray c2 = IMG_AT(src, i-1, j+1);
          gray c3 = IMG_AT(src, i+1, j-1);
          gray c4 = IMG_AT(src, i+1, j+1);

          gray s1 = IMG_AT(src, i, j);
          gray s2 = IMG_AT(src, i, j-1);
          gray s3 = IMG_AT(src, i, j);
          gray s4 = IMG_AT(src, i, j+1);

          IMG_AT(dest, i, j) = (
                  c1 + c2 + c3 + c4
                  + 2 * s1 + 2 * s2 + 2 * s3 + 2 * s4
                  + 4 * g) / 16;
      }
    }

    write_image(dest, true);

    free_image(dest);
    free_image(src);

    return 0;
}

