#include "dmap.h"
#include <float.h>
#include <limits.h>

dmap_t* dmap_create_empty(int cols, int rows, int maxval) {
    dmap_t* dmap = malloc(sizeof(dmap_t));
    dmap->cols = cols;
    dmap->rows = rows;
    dmap->maxval = maxval;
    dmap->map = calloc(dmap->cols * dmap->rows, sizeof(double));

    return dmap;
}

dmap_t* dmap_create_empty_pgm(pgm_t* src) {
    return dmap_create_empty(src->cols, src->rows, src->maxval);
}

dmap_t* dmap_create_empty_dmap(dmap_t* src) {
    return dmap_create_empty(src->cols, src->rows, src->maxval);
}

dmap_t* dmap_from_pgm(pgm_t* pgm) {
    dmap_t* dmap = dmap_create_empty(pgm->cols, pgm->rows, pgm->maxval);

    for (int i=0; i < pgm->rows; i++) {
        for (int j=0; j < pgm->cols ; j++) {
            DBL_AT(dmap, i, j) = (double) PGM_AT(pgm, i, j);
        }
    }

    return dmap;
}

dmap_t* dmap_copy(dmap_t* src) {
    dmap_t* cpy = dmap_create_empty(src->cols, src->rows, src->maxval);
    memcpy(cpy->map, src->map, src->cols * src->rows * sizeof(gray));
    return cpy;
}

pgm_t* dmap_to_pgm(dmap_t* dmap, bool reverse) {
    pgm_t* pgm = pgm_create_empty(dmap->cols, dmap->rows, dmap->maxval);

    double min = DBL_MAX;
    double max = DBL_MIN;
    for (int i=0; i < dmap->rows; i++) {
        for (int j=0; j < dmap->cols ; j++) {
            if (min > DBL_AT(dmap, i, j)) {
                min = DBL_AT(dmap, i, j);
            }
            if (max < DBL_AT(dmap, i, j)) {
                max = DBL_AT(dmap, i, j);
            }
        }
    }

    if (min == max) {
        return pgm;
    }

    for (int i=0; i < dmap->rows; i++) {
        for (int j=0; j < dmap->cols ; j++) {
            int gray = (int) ((DBL_AT(dmap, i, j) - min) * dmap->maxval / (max - min));
            PGM_AT(pgm, i, j) = reverse ? max - gray : gray;
        }
    }

    return pgm;
}

void dmap_free(dmap_t* dmap) {
    free(dmap->map);
    free(dmap);
}