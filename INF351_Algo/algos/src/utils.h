#ifndef __UTILS__
#define __UTILS__

int cmp_cpt;
int ass_cpt;

#define CMP(a, b) (cmp_cpt++, a < b)
#define SWAP(tab, i, j) { \
    ass_cpt+=2;           \
    int tmp = tab[i];     \
    tab[i] = tab[j];      \
    tab[j] = tmp;         \
}                         \

void print_tab(int tab[], int n);

void print_subtab(int tab[], int from, int to);

#endif