#ifndef __PGM_IO__
#define __PGM_IO__

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include "Util.h"

#define IMG_AT(img, i, j) ((img)->graymap[(i) * (img)->cols + (j)])

typedef enum {
	ALPHA,
	BINARY
} pixel_format_t;

typedef struct {
	int cols;
	int rows;
	int maxval;
	gray* graymap;
} img_t;


img_t* create_empty_image(int cols, int rows, int maxval);

img_t* read_image(char* filename);

img_t* copy_image(img_t* img);

void write_image(img_t* img, pixel_format_t pixel_format);

void free_image(img_t* img);

#endif