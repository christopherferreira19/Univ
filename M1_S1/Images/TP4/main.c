#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "pgm_io.h"
#include "dbls.h"

dbls_t* sobel_x(pgm_t* src) {
    dbls_t* dest = dbls_from_pgm(src);
    for (int i = 1; i < dest->rows - 1; i++) {
        for (int j = 1; j < dest->cols - 1; j++) {
            double v1 = -PGM_AT(src, i-1, j-1);
            double v2 = -PGM_AT(src, i  , j-1) * 2;
            double v3 = -PGM_AT(src, i+1, j-1);
            double v4 =  PGM_AT(src, i-1, j+1);
            double v5 =  PGM_AT(src, i  , j+1) * 2;
            double v6 =  PGM_AT(src, i+1, j+1);

            DBL_AT(dest, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return dest;
}

dbls_t* sobel_y(pgm_t* src) {
    dbls_t* dest = dbls_from_pgm(src);
    for (int i = 1; i < dest->rows - 1; i++) {
        for (int j = 1; j < dest->cols - 1; j++) {
            double v1 = -PGM_AT(src, i-1, j-1);
            double v2 = -PGM_AT(src, i-1, j  ) * 2;
            double v3 = -PGM_AT(src, i-1, j+1);
            double v4 =  PGM_AT(src, i+1, j-1);
            double v5 =  PGM_AT(src, i+1, j  ) * 2;
            double v6 =  PGM_AT(src, i+1, j+1);

            DBL_AT(dest, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return dest;
}

dbls_t* magnitude(pgm_t* src, dbls_t* gradx, dbls_t* grady) {
    dbls_t* res = dbls_from_pgm(src);
    for (int i = 1; i < res->rows - 1; i++) {
        for (int j = 1; j < res->cols - 1; j++) {
            double x = DBL_AT(gradx, i, j);
            double y = DBL_AT(grady, i, j);
            DBL_AT(res, i, j) = sqrt(x * x + y * y);
        }
    }

    return res;
}

dbls_t* multiply(pgm_t* src, dbls_t* left, dbls_t* right) {
    dbls_t* res = dbls_from_pgm(src);
    for (int i = 1; i < res->rows - 1; i++) {
        for (int j = 1; j < res->cols - 1; j++) {
            double l = DBL_AT(left, i, j);
            double r = DBL_AT(right, i, j);
            DBL_AT(res, i, j) = l * r;
        }
    }

    return res;
}

void dbls_write(dbls_t* dbls, char* base, char* suffix) {
    pgm_t* pgm = dbls_to_pgm(dbls);
    pgm_write_filename(pgm, BINARY, base, suffix);
    free(pgm);
}

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
        printf("\nUsage: %s <file>\n\n", argv[0]);
        exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);
    
    dbls_t* sobx = sobel_x(src);
    dbls_t* soby = sobel_y(src);
    dbls_t* sobm = magnitude(src, sobx, soby);
    dbls_t* mxx = multiply(src, sobx, sobx);
    dbls_t* myy = multiply(src, soby, soby);
    dbls_t* mxy = multiply(src, sobx, soby);

    dbls_write(sobx, argv[1], "sobel_x");
    dbls_write(soby, argv[1], "sobel_y");
    dbls_write(sobm, argv[1], "sobel_magnitude");
    dbls_write(mxx, argv[1], "xx");
    dbls_write(myy, argv[1], "yy");
    dbls_write(mxy, argv[1], "xy");

    pgm_free(src);
    dbls_free(sobx);
    dbls_free(soby);

    return 0;
}
