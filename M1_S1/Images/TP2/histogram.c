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

    for (int i = 0; i < 256; i++) {
        printf("%d %d\n", i, histo[i]);
    }

    free_image(src);

    return 0;
}