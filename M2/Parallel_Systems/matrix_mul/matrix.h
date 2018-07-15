#ifndef __MATRIX_H__
#define __MATRIX_H__

#include <time.h>
#include <stdlib.h>
#include <stdio.h>

#define MAX_SIZE 1 << 10

typedef double matrix[MAX_SIZE][MAX_SIZE];

void matrix_init(matrix m, int n);

void matrix_init_random(matrix m, int n, int max);

void matrix_print(matrix m, int n);

void matrix_transpose(matrix from, matrix to, int n);

#endif
