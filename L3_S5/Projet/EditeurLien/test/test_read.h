#ifndef __TEST_READ_H__
#define __TEST_READ_H__

char* test_libelf_header(char* filename);
char* test_libelf_section(char* filename);
char* test_libelf_symbols(char* filename);
char* test_libelf_relocate(char* filename);
char* test_example1_relocation_section();

#endif