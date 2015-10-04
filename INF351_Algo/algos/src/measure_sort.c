#include "stdlib.h"
#include "stdio.h"
#include "time.h"
#include <sys/time.h>
#include "string.h"

#include "utils.h"
#include "easy_sort.h"
#include "quicksort.h"

#define N 100000
#define VALUE_MAX 100

void measure_sort(char* name, int tab[], void (*sort)(int n, int tab[])) {
    struct timeval start;
    struct timeval end;

    cmp_cpt = 0;
    ass_cpt = 0;

    int tab_to_sort[N];
    memcpy(tab_to_sort, tab, N * sizeof(int));
    gettimeofday(&start, NULL);
    sort(N, tab_to_sort);
    gettimeofday(&end, NULL);

    printf("%s : %12d %12d %12ld %12ld\n", name, cmp_cpt, ass_cpt, end.tv_sec - start.tv_sec, end.tv_usec - start.tv_usec);
}

int main() {
    int tab[N];
    srand(time(NULL));
    for (int i = 0; i < N; i++) {
        tab[i] = rand() % (VALUE_MAX * 2) - VALUE_MAX;
    }

    printf("Tableau : ");
    print_tab(tab, N);
    printf("\n\n");

    printf("                         Comp       Assign      Seconds     uSeconds\n");
    measure_sort("Selection Sort", tab, &selection_sort);
    measure_sort("Insertion Sort", tab, &insertion_sort);
    measure_sort("Quick Sort (F)", tab, &quick_sort_first);
    measure_sort("Quick Sort (R)", tab, &quick_sort_random);
    //measure_sort("Bubble Sort   ", tab, &bubble_sort);

    return 0;
}