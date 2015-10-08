#include "stdlib.h"
#include "stdio.h"

int polynome_value(int coeff[], int coeff_n, int x) {
    int value = coeff[0];
    int x_pow = x;
    for (int i = 1; i < coeff_n; i++) {
        value += coeff[i] * x_pow;
        x_pow *= x;
    }

    return value;
}

int polynome_horner(int coeff[], int coeff_n, int x) {
    int i = coeff_n - 1;
    int value = coeff[i];
    while (i > 0) value = value * x + coeff[--i];
    return value;
}

int main() {
    int coeff[3] = { 3, 2, 1 };
    printf("Value Naive : %d\n", polynome_value(coeff, 3, 2));
    printf("Value Mieux : %d\n", polynome_horner(coeff, 3, 2));
    return 0;
}