#ifndef __DMAP_H__
#define __DMAP_H__

#include "pgm_io.h"

#define DBL_AT(dbls, i, j) ((dbls)->map[(i) * (dbls)->cols + (j)])

typedef struct {
	int cols;
	int rows;
	int maxval;
	double* map;
} dmap_t;

dmap_t* dmap_create_empty_from(pgm_t* src);

dmap_t* dmap_create_empty(int cols, int rows, int maxval);

dmap_t* dmap_from_pgm(pgm_t* src);

dmap_t* dmap_copy(dmap_t* src);

pgm_t* dmap_to_pgm(dmap_t* src);

void dmap_free(dmap_t* dmap);

#endif