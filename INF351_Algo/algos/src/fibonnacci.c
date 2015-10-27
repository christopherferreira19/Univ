#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>

int compute_fib(int t[], int n) {
    if (n < 2) {
        return n;
    }

    if (t[n - 2] == 0) {
        t[n - 2] = compute_fib(t, n - 2) + compute_fib(t, n - 1);
    }

    return t[n - 2];
}

int fib_tab(int n) {
    if (n < 2) {
        return n;
    }

    int* t = calloc(n - 1, sizeof(int));
    int result = compute_fib(t, n);
    free(t);

    return result;
}

int fib_rec(int n) {
    if (n < 2) {
        return n;
    }

    return fib_rec(n - 1) + fib_rec(n - 2);
}

int fib_var(int n) {
    if (n < 2) {
        return n;
    }

    int minus2 = 0;
    int minus1 = 1;
    int i = n - 1;
    while (i-- > 0) {
        int next = minus2 + minus1;
        minus2 = minus1;
        minus1 = next;
    }

    return minus1;
}

void test_fib(char* name, int n, int (*fib)(int)) {
    struct timeval start;
    struct timeval end;

    gettimeofday(&start, NULL);
    int result = fib(n);
    gettimeofday(&end, NULL);
    printf("%s(%d) = %d [%ld %ld]\n", name, n, result, end.tv_sec - start.tv_sec, end.tv_usec - start.tv_usec);
}

void test_fibs(int n) {
    struct timeval start;
    struct timeval end;

    test_fib("fib_rec", n, fib_rec);
    test_fib("fib_tab", n, fib_tab);
    test_fib("fib_var", n, fib_var);
}

int main() {
    for (int i = 0; i < 21; i++) {
        test_fibs(i);
    }

    int big_n = 45000;
    test_fib("fib_tab", big_n, fib_tab);
    test_fib("fib_var", big_n, fib_var);

    return 0;
}