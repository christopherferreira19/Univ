#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

int binomial_filter[5][5] = {
   { 1,  4,  6,  4,  1 },
   { 4, 16, 24, 16,  4 },
   { 6, 24, 36, 24,  6 },
   { 4, 16, 24, 16,  4 },
   { 1,  4,  6,  4,  1 }
};

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
      printf("\nUsage: %s file \n\n", argv[0]);
      exit(0);
    }

    img_t* src = read_image(argv[1]);
    img_t* dest = create_empty_image(src->cols, src->rows, src->maxval);
  
    for (int i = 2; i < src->rows - 2; i++) {
      for (int j = 2; j < src->cols - 2; j++) {
          int sum = 0;

          for (int u = 0; u < 5; u++) {
              for (int v = 0; v < 5; v++) {
                  sum += binomial_filter[u][v] * IMG_AT(src, i-2+u, j-2+v);
              }
          }

          IMG_AT(dest, i, j) = sum / 256;
      }
    }

    write_image(dest, true);

    free_image(dest);
    free_image(src);

    return 0;
}

