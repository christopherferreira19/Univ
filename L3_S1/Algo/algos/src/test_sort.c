#include "stdlib.h"
#include "stdio.h"
#include "stdbool.h"
#include "time.h"
#include "string.h"

#include "utils.h"
#include "easy_sort.h"
#include "quicksort.h"

#define REPEAT 100
#define N 1000
#define VALUE_MAX 100000

void generate_random_tab(int tab[]) {
    for (int i = 0; i < N; i++) {
        tab[i] = rand() % (VALUE_MAX * 2) - VALUE_MAX;
    }
}

bool verify_sorted(int tab[]) {
    for (int i = 1; i < N; i++) {
        if (tab[i - 1] > tab[i]) {
            return false;
        }
    }

    return true;
}

bool verify_same_elements(int tab1[], int tab2[]) {
    for (int i = 0; i < N; i++) {
        int cpt1 = 0;
        for (int j = 0; j < N; j++) {
            if (tab1[i] == tab1[j]) {
                cpt1++;
            }
        }

        int cpt2 = 0;
        for (int j = 0; j < N; j++) {
            if (tab1[i] == tab2[j]) {
                cpt2++;
            }
        }

        if (cpt1 != cpt2) {
            return false;
        }
    }

    return true;
}

void test_sort(char* name, int unsorted[], void (*sort)(int n, int tab[])) {
    int sorted[N];
    memcpy(sorted, unsorted, N * sizeof(int));
    sort(N, sorted);

    if (!verify_sorted(sorted)
            || !verify_same_elements(unsorted, sorted)) {
        printf("[%s] invalid :\n", name);
        printf("Unsorted ");
        print_tab(unsorted, N);
        printf("\n  Sorted ");
        print_tab(sorted, N);
        printf("\n");
    }
}

int main(int argc, char** argv) {
    int tab[N];

    unsigned int seed;
    if (argc > 1) {
        seed = strtoul(argv[1], NULL, 10);
    }
    else {
        seed = time(NULL);
    }

    printf("Seed %u\n", seed);
    srand(seed);

    for (int i = 0; i < N; i++) {
        tab[i] = 1;
    }
    //test_sort("Selection ", tab, selection_sort);
    //test_sort("Insertion ", tab, insertion_sort);
    test_sort("Quick (F)", tab, &quick_sort_first);
    test_sort("Quick (R)", tab, &quick_sort_random);
    //test_sort("Bubble", tab, bubble_sort);

    for (int i = 0; i < N; i++) {
        tab[i] = 3;
        //test_sort("Selection ", tab, selection_sort);
        //test_sort("Insertion ", tab, insertion_sort);
        test_sort("Quick (F)", tab, &quick_sort_first);
        test_sort("Quick (R)", tab, &quick_sort_random);
        //test_sort("Bubble", tab, bubble_sort);
        tab[i] = 1;
    }

    for (int i = 0; i < REPEAT; i++) {
        generate_random_tab(tab);

        //test_sort("Selection ", tab, selection_sort);
        //test_sort("Insertion ", tab, insertion_sort);
        test_sort("Quick (F)", tab, &quick_sort_first);
        test_sort("Quick (R)", tab, &quick_sort_random);
        //test_sort("Bubble", tab, bubble_sort);
    }

    return 0;
}
