#include "utils.h"
#include "stdio.h"

void print_tab(int tab[], int n) {
    if (n > 20) {
        return;
    }

    printf("[");
    if (n > 0) {
        printf("%d", tab[0]);

        for (int i = 1; i < n; i++) {
            printf(", %d", tab[i]);
        }
    }

    printf("]");
}

void print_subtab(int tab[], int from, int to) {
    int size = to - from + 1;
    if (size > 20) {
        return;
    }

    printf("[");
    if (size > 0) {
        printf("%d", tab[from]);

        for (int i = from + 1; i <= to; i++) {
            printf(", %d", tab[i]);
        }
    }

    printf("]");
}