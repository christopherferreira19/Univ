#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "pgm_io.h"

#define N 3
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

    img_t* src = read_image(argv[1]);

    img_t* dest = malloc(sizeof(img_t));
    dest->cols = src->cols;
    dest->rows = src->rows;
    dest->maxval = src->maxval;
    dest->graymap = malloc(dest->cols * dest->rows);
    
    for (int i = 2; i < src->rows - 2; i++) {
      for (int j = 2; j < src->cols - 2; j++) {
          for (int u = 0; u < N; u++) {
              for (int v = 0; v < N; v++) {
                  values[u * N + v] = src->graymap[(i-2+u) * src->cols + (j-2+v)];
              }
          }

          qsort(values, N * N, sizeof(int), gray_compare);
          dest->graymap[i * dest->cols + j] = values[MEDIAN_INDEX];
      }
    }
    write_image(dest, true);

    return 0;
}

