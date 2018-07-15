#include <stdio.h>
#include "matrix.h"

matrix m1;
matrix m2;
matrix m3;
int n = 10;

void matrix_mult(matrix m1, matrix m2, matrix m3) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      for (int k = 0; k < n; k++) {
        m3[i][j] += m1[i][k] * m2[k][j];
			}
		}
	}
}

int main(int argc, char* argv[]) {
  srand(time(NULL));

  matrix_init_random(m1, n, 30);
  matrix_print(m1, n);
  matrix_init_random(m2, n, 30);
  matrix_print(m2, n);

  matrix_mult(m1, m2, m3);

  matrix_print(m3, n);
}
