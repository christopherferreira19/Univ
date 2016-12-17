#include "dbls.h"

dbls_t* dbls_create_empty(int cols, int rows, int maxval) {
    dbls_t* dbls = malloc(sizeof(dbls_t));
    dbls->cols = cols;
    dbls->rows = rows;
    dbls->maxval = maxval;
    dbls->map = malloc(dbls->cols * dbls->rows * sizeof(double));

    return dbls;
}

dbls_t* dbls_from_pgm(pgm_t* pgm) {
    dbls_t* dbls = dbls_create_empty(pgm->cols, pgm->rows, pgm->maxval);

    for (int i=0; i < pgm->rows; i++) {
        for (int j=0; j < pgm->cols ; j++) {
        	DBL_AT(dbls, i, j) = (double) PGM_AT(pgm, i, j);
        }
    }

    return dbls;
}

pgm_t* dbls_to_pgm(dbls_t* dbls) {
	pgm_t* pgm = pgm_create_empty(dbls->cols, dbls->rows, dbls->maxval);

	double max = 0;
    for (int i=0; i < dbls->rows; i++) {
        for (int j=0; j < dbls->cols ; j++) {
        	if (max < DBL_AT(dbls, i, j)) {
        		max = DBL_AT(dbls, i, j);
        	}
        }
    }

    if (max == 0) {
    	return pgm;
    }

    for (int i=0; i < dbls->rows; i++) {
        for (int j=0; j < dbls->cols ; j++) {
        	PGM_AT(pgm, i, j) = (int) (DBL_AT(dbls, i, j) * dbls->maxval / max);
        }
    }

    return pgm;
}

void dbls_free(dbls_t* dbls) {
    free(dbls->map);
    free(dbls);
}