#include "stdbool.h"
#include "utils.h"
#include "easy_sort.h"

void selection_sort(int n, int tab[]) {
    for (int i = 0; i < n - 1; i++) {
        int min_index = i;
        for (int j = i + 1; j < n; j++) {
            if (CMP(tab[j], tab[min_index])) {
                min_index = j;
            }
        }

        SWAP(tab, i, min_index);
    }
}

void insertion_sort(int n, int tab[]) {
    for (int i = 1; i < n; i++) {
        int tmp = tab[i];
        int j = i - 1;
        while (j >= 0 && CMP(tmp, tab[j])) {
            ass_cpt++;
            tab[j + 1] = tab[j];
            j--;
        }

        tab[j + 1] = tmp;
    }
}

void bubble_sort(int n, int tab[]) {
    bool done = false;
    while (!done) {
        done = true;
        for (int i = 0; i < n - 1; i++) {
            if (CMP(tab[i + 1], tab[i])) {
                SWAP(tab, i, i + 1);
                done = false;
            }
        }
    }
}
