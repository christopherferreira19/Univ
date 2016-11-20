#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

#define N 3
#define HALF_N (N / 2)
#define MEDIAN_INDEX ((N * N) + 1) / 2

gray values[N * N];

static int gray_compare(const void* p1, const void* p2) {
    return *((int*) p1) - *((int*) p2);
}

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
      printf("\nUsage: %s file \n\n", argv[0]);
      exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);
    pgm_t* dest = pgm_create_empty(src->cols, src->rows, src->maxval);
    
    for (int i = HALF_N; i < src->rows - HALF_N; i++) {
      for (int j = HALF_N; j < src->cols - HALF_N; j++) {
          for (int u = 0; u < N; u++) {
              for (int v = 0; v < N; v++) {
                  values[u * N + v] = PGM_AT(src, i-HALF_N+u, j-HALF_N+v);
              }
          }

          qsort(values, N * N, sizeof(int), gray_compare);
          PGM_AT(dest, i, j) = values[MEDIAN_INDEX];
      }
    }

    pgm_write(dest, true);

    pgm_free(dest);
    pgm_free(src);

    return 0;
}

