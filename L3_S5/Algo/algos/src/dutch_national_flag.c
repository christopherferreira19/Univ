#include "stdio.h"
#include "utils.h"

void dutch_national_flag(int tab[], int n) {
    int i1 = 0;
    int i2 = 0;
    int i3 = n - 1;

    int tmp;
    while (i2 <= i3) {
        switch (tab[i2]) {
            case 1:
                SWAP(tab, i1, i2);
                i1++;
                i2++;
                break;
            case 2:
                i2++;
                break;
            case 3:
                SWAP(tab, i2, i3);
                i3--;
                break;
        }
    }
}

int main() {
    #define n 5
    int ok[n] = { 1, 3, 2, 1, 2 };
    int pas_bien[n] = { 1, 2, 1, 3, 2 };

    dutch_national_flag(ok, n);
    dutch_national_flag(pas_bien, n);

    print_tab(ok, n);
    printf("\n");
    print_tab(pas_bien, n);
    printf("\n");
    return 0;
}