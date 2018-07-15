#include "matrix.h"

void matrix_init(matrix m, int n) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      m[i][j] = i * n + j;
    }
  }
}

void matrix_init_random(matrix m, int n, int max) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      m[i][j] = (double) (rand() % max);
    }
  }
}

void matrix_print(matrix m, int n) {
  for (int i = 0 ; i < n; i++) {
    for (int j = 0 ; j < n; j++) {
      printf ("%6.2f ", m[i][j]);
    }
    printf ("\n");
  }

  printf ("\n");
}

void matrix_transpose(matrix from, matrix to, int n) {
  for (int i = 0 ; i < n; i++) {
    for (int j = 0 ; j < n; j++) {
      to[j][i] = from[i][j];
    }
  }
}

