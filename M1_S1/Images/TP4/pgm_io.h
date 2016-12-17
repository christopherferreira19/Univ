#ifndef __PGM_IO__
#define __PGM_IO__

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include "Util.h"

#define PGM_AT(img, i, j) ((img)->graymap[(i) * (img)->cols + (j)])

typedef enum {
	ALPHA,
	BINARY
} pixel_format_t;

typedef struct {
	int cols;
	int rows;
	int maxval;
	gray* graymap;
} pgm_t;


pgm_t* pgm_create_empty(int cols, int rows, int maxval);

pgm_t* pgm_read(char* filename);

pgm_t* pgm_copy(pgm_t* img);

void pgm_write(pgm_t* img, pixel_format_t pixel_format);

void pgm_write_filename(pgm_t* img, pixel_format_t pixel_format, char* filename, char* suffix);

void pgm_write_file(pgm_t* img, pixel_format_t pixel_format, FILE* file);

void pgm_free(pgm_t* img);

#endif