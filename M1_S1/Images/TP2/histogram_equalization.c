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

    int histo[256] = {0, };

    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            histo[IMG_AT(src, i, j)]++;
        }
    }

    for (int i = 1; i < 256; i++) {
        histo[i] += histo[i - 1];
    }

    img_t* dest = create_empty_image(src->cols, src->rows, src->maxval);

    int minval = 0;
    while (histo[minval] == 0) minval++;
    int maxval = 255;
    while (histo[maxval] == 0) maxval--;

    int total_pixels = src->cols * src->rows;
    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            IMG_AT(dest, i, j) = src->maxval * (histo[IMG_AT(src, i, j)]) / total_pixels;
        }
    }

    write_image(dest, true);

    free_image(dest);
    free_image(src);

    return 0;
}