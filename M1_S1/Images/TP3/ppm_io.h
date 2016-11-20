#ifndef __PGM_IO__
#define __PGM_IO__

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>
#include "Util.h"

#define PPM_AT(ppm, i, j) ((ppm)->pixmap[(i) * (ppm)->cols + (j)])

typedef struct {
    uint8_t red;
    uint8_t green;
    uint8_t blue;
} color;

typedef enum {
	ALPHA,
	BINARY
} pixel_format_t;

typedef struct {
	int cols;
	int rows;
	int maxval;
	color* pixmap;
} ppm_t;


ppm_t* ppm_create_empty(int cols, int rows, int maxval);

ppm_t* ppm_read(char* filename);

ppm_t* ppm_copy(ppm_t* ppm);

void ppm_write(ppm_t* ppm, pixel_format_t pixel_format);

void ppm_free(ppm_t* ppm);

#endif