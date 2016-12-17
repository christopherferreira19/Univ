#ifndef __DBLS_H__
#define __DBLS_H__

#include "pgm_io.h"

#define DBL_AT(floats, i, j) ((floats)->map[(i) * (floats)->cols + (j)])

typedef struct {
	int cols;
	int rows;
	int maxval;
	double* map;
} dbls_t;

dbls_t* dbls_create_empty(int cols, int rows, int maxval);

dbls_t* dbls_from_pgm(pgm_t* src);

pgm_t* dbls_to_pgm(dbls_t* src);

void dbls_free(dbls_t* dbls);

#endif