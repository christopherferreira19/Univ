#ifndef __TEST_RELOCATE_H__
#define __TEST_RELOCATE_H__

char* test_relocate_invalid();
char* test_relocate_overlap1();
char* test_relocate_overlap2();

char* test_relocate_basic();
char* test_relocate_3_sections();
char* test_relocate_alignment();
char* test_relocate_abs();
char* test_relocate_jump24_call();

#endif