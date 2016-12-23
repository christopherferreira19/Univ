#include <stdlib.h>
#include <stdio.h>
#include <float.h>
#include <string.h>
#include <math.h>
#include "pgm_io.h"
#include "dmap.h"

dmap_t* sobel_x(pgm_t* src) {
    dmap_t* res = dmap_create_empty_pgm(src);
    for (int i = 1; i < res->rows - 1; i++) {
        for (int j = 1; j < res->cols - 1; j++) {
            double v1 = -PGM_AT(src, i-1, j-1);
            double v2 = -PGM_AT(src, i  , j-1) * 2;
            double v3 = -PGM_AT(src, i+1, j-1);
            double v4 =  PGM_AT(src, i-1, j+1);
            double v5 =  PGM_AT(src, i  , j+1) * 2;
            double v6 =  PGM_AT(src, i+1, j+1);

            DBL_AT(res, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return res;
}

dmap_t* sobel_y(pgm_t* src) {
    dmap_t* res = dmap_create_empty_pgm(src);
    for (int i = 1; i < res->rows - 1; i++) {
        for (int j = 1; j < res->cols - 1; j++) {
            double v1 = -PGM_AT(src, i-1, j-1);
            double v2 = -PGM_AT(src, i-1, j  ) * 2;
            double v3 = -PGM_AT(src, i-1, j+1);
            double v4 =  PGM_AT(src, i+1, j-1);
            double v5 =  PGM_AT(src, i+1, j  ) * 2;
            double v6 =  PGM_AT(src, i+1, j+1);

            DBL_AT(res, i, j) = (v1 + v2 + v3 + v4 + v5 + v6) / 4;
        }
    }

    return res;
}

dmap_t* magnitude(pgm_t* src, dmap_t* gradx, dmap_t* grady) {
    dmap_t* res = dmap_create_empty_pgm(src);
    for (int i = 0; i < res->rows; i++) {
        for (int j = 0; j < res->cols; j++) {
            double x = DBL_AT(gradx, i, j);
            double y = DBL_AT(grady, i, j);
            DBL_AT(res, i, j) = sqrt(x * x + y * y);
        }
    }

    return res;
}

dmap_t* multiply(pgm_t* src, dmap_t* left, dmap_t* right) {
    dmap_t* res = dmap_create_empty_pgm(src);
    for (int i = 0; i < res->rows; i++) {
        for (int j = 0; j < res->cols; j++) {
            double l = DBL_AT(left, i, j);
            double r = DBL_AT(right, i, j);
            DBL_AT(res, i, j) = l * r;
        }
    }

    return res;
}

dmap_t* binomial_filter(dmap_t* src) {
    dmap_t* res = dmap_copy(src);     
    for (int i = 1; i < res->rows - 1; i++) {
        for (int j = 1; j < res->cols - 1; j++) {
            double g = DBL_AT(src, i, j);

            double c1 = DBL_AT(src, i-1, j-1);
            double c2 = DBL_AT(src, i-1, j+1);
            double c3 = DBL_AT(src, i+1, j-1);
            double c4 = DBL_AT(src, i+1, j+1);

            double s1 = DBL_AT(src, i-1, j);
            double s2 = DBL_AT(src, i, j-1);
            double s3 = DBL_AT(src, i+1, j);
            double s4 = DBL_AT(src, i, j+1);

            DBL_AT(res, i, j) = (
                    c1 + c2 + c3 + c4
                    + 2 * s1 + 2 * s2 + 2 * s3 + 2 * s4
                    + 4 * g) / 16;
        }
    }

    return res;
}

dmap_t* harris(pgm_t* src, dmap_t* ixx, dmap_t* iyy, dmap_t* ixy, double alpha) {
    dmap_t* res = dmap_create_empty_pgm(src);
    for (int i = 0; i < res->rows; i++) {
        for (int j = 0; j < res->cols; j++) {
            double xx = DBL_AT(ixx, i, j);
            double yy = DBL_AT(iyy, i, j);
            double xy = DBL_AT(ixy, i, j);
            double det = xx * yy - xy * xy;
            double trace = (xx + yy) * (xx + yy);

            DBL_AT(res, i, j) = det - alpha * trace;
        }
    }

    return res;
}

dmap_t* maxima(dmap_t* src, double n) {
    double* maxima = calloc(n, sizeof(double));

    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            double v = DBL_AT(src, i, j);
            int i = 0;
            while (i < n && v > maxima[i]) {
                i++;
            }

            if (i > 0) {
                maxima[i - 1] = v;
            }
        }
    }

    dmap_t* res = dmap_create_empty_dmap(src);
    for (int i = 0; i < res->rows; i++) {
        for (int j = 0; j < res->cols; j++) {
            double v = DBL_AT(src, i, j);
            if (v >= maxima[0]) {
                DBL_AT(res, i, j) = v;
            }
        }
    }

    return res;
}

dmap_t* minima(dmap_t* src, double n) {
    double* maxima = malloc(n * sizeof(double));
    for (int i = 0; i < n; i++) {
        maxima[i] = DBL_MIN;
    }

    for (int i = 0; i < src->rows; i++) {
        for (int j = 0; j < src->cols; j++) {
            double v = DBL_AT(src, i, j);
            int i = 0;
            while (i < n && v < maxima[i]) {
                i++;
            }

            if (i > 0) {
                maxima[i - 1] = v;
            }
        }
    }

    dmap_t* res = dmap_create_empty_dmap(src);
    for (int i = 0; i < res->rows; i++) {
        for (int j = 0; j < res->cols; j++) {
            double v = DBL_AT(src, i, j);
            if (v <= maxima[0]) {
                DBL_AT(res, i, j) = v;
            }
        }
    }

    return res;
}

void dmap_write(dmap_t* dmap, bool reverse, char* base, char* suffix) {
    pgm_t* pgm = dmap_to_pgm(dmap, reverse);
    pgm_write_filename(pgm, BINARY, base, suffix);
    pgm_free(pgm);
}

int main(int argc, char* argv[]) {
    /* Arguments */
    if ( argc != 2 ) {
        printf("\nUsage: %s <file>\n\n", argv[0]);
        exit(0);
    }

    pgm_t* src = pgm_read(argv[1]);
    
    dmap_t* sobx = sobel_x(src);
    dmap_t* soby = sobel_y(src);
    dmap_t* sobm = magnitude(src, sobx, soby);
    dmap_t* mxx = multiply(src, sobx, sobx);
    dmap_t* myy = multiply(src, soby, soby);
    dmap_t* mxy = multiply(src, sobx, soby);
    dmap_t* mxxf = binomial_filter(mxx);
    dmap_t* myyf = binomial_filter(myy);
    dmap_t* mxyf = binomial_filter(mxy);
    dmap_t* harris1 = harris(src, mxxf, myyf, mxyf, 1);

    dmap_t* max[10];
    for (int i = 1; i < 10; i++) {
        max[i] = minima(harris1, i);
    }
    dmap_t* max10 = minima(harris1, 10);
    dmap_t* max20 = minima(harris1, 20);
    dmap_t* max30 = minima(harris1, 30);

    dmap_write(sobx, false, argv[1], "sobel_x");
    dmap_write(soby, false, argv[1], "sobel_y");
    dmap_write(sobm, false, argv[1], "sobel_magnitude");

    dmap_write(mxx, false, argv[1], "xx");
    dmap_write(myy, false, argv[1], "yy");
    dmap_write(mxy, false, argv[1], "xy");

    dmap_write(mxxf, false, argv[1], "xxf");
    dmap_write(myyf, false, argv[1], "yyf");
    dmap_write(mxyf, false, argv[1], "xyf");

    char buf[1024];
    dmap_write(harris1, true, argv[1], "harris1");
    for (int i = 1; i < 10; i++) {
        sprintf(buf, "max%d", i);
        dmap_write(max[i], true, argv[1], buf);
    }
    dmap_write(max10, true, argv[1], "max10");
    dmap_write(max20, true, argv[1], "max20");
    dmap_write(max30, true, argv[1], "max30");

    pgm_free(src);
    dmap_free(sobx);
    dmap_free(soby);
    dmap_free(sobm);
    dmap_free(mxx);
    dmap_free(myy);
    dmap_free(mxy);
    dmap_free(mxxf);
    dmap_free(myyf);
    dmap_free(mxyf);

    dmap_free(harris1);

    for (int i = 1; i < 10; i++) {
        dmap_free(max[i]);
    }
    dmap_free(max10);
    dmap_free(max20);
    dmap_free(max30);

    return 0;
}
