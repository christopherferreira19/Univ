#include "stdlib.h"
#include "stdio.h"

#include "utils.h"
#include "quicksort.h"

static int random_pivot(int tab[], int from, int to) {
    return tab[from + (rand() % (to - from + 1))];
}

static int first_pivot(int tab[], int from, int to) {
    return tab[from];
}

void quick_sort_rec(int tab[], int from, int to, int (*pivot_fn)(int tab[], int from, int to)) {
    if (from >= to) {
        return;
    }

    int pivot = pivot_fn(tab, from, to);

    int left   = from;
    int middle = from;
    int right  = to;

    while (middle <= right) {
        int cmp = CMP(tab[middle], pivot);
        if (tab[middle] == pivot) {
            middle++;
        }
        else if (cmp) {
            SWAP(tab, left, middle);
            left++;
            middle++;
        }
        else {
            SWAP(tab, middle, right);
            right--;
        }
    }

    quick_sort_rec(tab, from, left - 1, pivot_fn);
    quick_sort_rec(tab, right + 1, to, pivot_fn);
}

void quick_sort_first(int n, int tab[]) {
    quick_sort_rec(tab, 0, n - 1, first_pivot);
}

void quick_sort_random(int n, int tab[]) {
    quick_sort_rec(tab, 0, n - 1, random_pivot);
}