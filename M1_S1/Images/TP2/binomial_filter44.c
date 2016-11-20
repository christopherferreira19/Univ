#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

#define FILTER_SIZE 5
#define FILTER_HALFSIZE (FILTER_SIZE / 2)
#define FILTER_SUM 256
int filter[FILTER_SIZE][FILTER_SIZE] = {
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

    pgm_t* src = pgm_read(argv[1]);
    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);

    for (int i = FILTER_HALFSIZE; i < src->rows - FILTER_HALFSIZE; i++) {
      for (int j = FILTER_HALFSIZE; j < src->cols - FILTER_HALFSIZE; j++) {
          int sum = 0;

          for (int u = 0; u < FILTER_SIZE; u++) {
              for (int v = 0; v < FILTER_SIZE; v++) {
                  sum += filter[u][v] * PGM_AT(src,
                      i - FILTER_HALFSIZE + u,
                      j - FILTER_HALFSIZE + v);
              }
          }

          PGM_AT(dest, i, j) = sum / FILTER_SUM;
      }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}

