#include "stdio.h"
#include "stdbool.h"
#include "stdint.h"

#define N 4

double h1[N] = { 2, 4, 6, 8 };
double h2[N] = { 8, 6, 4, 2 };

bool d(double g[], int value, double b) {
    return g[value] + b > 0;
}

void print(char* name, double a[]) {
    /*printf("%5s = { ", name);
    for (int i = 0; i < N; i++) {
        printf(" %d ->  %2$5.2lf, ", i + 1, a[i]);
    }
    printf("}\n");*/
}

int main() {
    print("h1", h1);
    print("h2", h2);

    double g[N];
    for (int i = 0; i < N; i++) {
        g[i] = h1[i] / (h1[i] + h2[i]);
    }
    print("g", g);

    double B[N];
    for (int i = 0; i < N; i++) {
        B[i] = -g[i];
    }
    print("B", B);

    double TP[N], TN[N], FP[N], FN[N];
    for (int i = 0; i < N; i++) {
        double b = B[i];
        for (int j = 0; j < N; j++) {
            if (d(g, j, b)) {
                TP[i] += h1[i];
                FP[i] += h2[i];
            }
            else {
                FN[i] += h1[i];
                TN[i] += h2[i];
            }
        }
    }

    print("TP", TP);
    print("TN", TN);
    print("FP", FP);
    print("FN", FN);

    double TPR[N];
    double FPR[N];
    for (int i = 0; i < N; i++) {
        TPR[i] = TP[i] / (TP[i] + FN[i]);
        FPR[i] = FP[i] / (FP[i] + TN[i]);
    }

    print("TPR", TPR);
    print("FPR", FPR);

    for (int i = 0; i < N; i++) {
        printf("%2$5.2lf\t%2$5.2lf\n", TPR[i], FPR[i]);
    }

    return 0;
}