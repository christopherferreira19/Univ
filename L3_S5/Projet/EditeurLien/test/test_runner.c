#include "test_lib.h"
#include "test_read.h"
#include "test_relocate.h"

#define FILES_AUTO_COUNT 8
static char* FILES_AUTO[FILES_AUTO_COUNT] = {
    "test/files/example1.o",
    "test/files/example1",
    "test/files/example2.o",
    "test/files/example2",
    "test/files/example3.o",
    "test/files/example3",
    "test/files/example4.o",
    "test/files/example4"
};

char* all_tests() {
    for (int i = 0; i < FILES_AUTO_COUNT; i++) {
        char* filename = FILES_AUTO[i];
        mu_run_tests_file(test_libelf_header, filename);
        mu_run_tests_file(test_libelf_section, filename);
        mu_run_tests_file(test_libelf_symbols, filename);
        mu_run_tests_file(test_libelf_relocate, filename); //!\ broken value
    }

    mu_run_test(test_example1_relocation_section);
    
    mu_run_test(test_relocate_invalid);
    mu_run_test(test_relocate_overlap1);
    mu_run_test(test_relocate_overlap2);

	mu_run_test(test_relocate_basic);
	mu_run_test(test_relocate_3_sections);
	mu_run_test(test_relocate_alignment);
    mu_run_test(test_relocate_abs);
    mu_run_test(test_relocate_jump24_call);

	return 0;
}

int main(int argc, char* argv[]) {
    char *result = all_tests();
    if (result != 0) {
        printf("ERROR : %s\n", result);
    }
    else {
        printf("ALL TESTS PASSED\n");
    }
    printf("Tests run: %d, Assertions : %d\n", tests_run, asserts_run);

    return result != 0;
}
