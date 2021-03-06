#ifndef   	_MEM_ALLOC_H_
#define   	_MEM_ALLOC_H_

/* Allocator functions, to be implemented in mem_alloc.c */
void memory_init(void); 
char *memory_alloc(int size); 
void memory_free(char *p);

/* to display info about memory leaks on program exit*/
void run_at_exit(void);


/* Logging functions */

/* to be called when a block has been successfully allocated */
/* addr: the address of the allocated region returned to the user */
/* size: the size of the block allocated to the user*/
void print_alloc_info(char *addr, int size); 

/* to be called on free*/
/* addr: the addr of the user buffer to be freed*/
void print_free_info(char *addr); 

/* to be called when an allocation fails*/
/* size: the size of the block requested by the user*/
void print_error_alloc(int size);
void print_error_free(void* addr);

void print_free_blocks(void);
void print_used_blocks();

void print_info(void); 
char *heap_base(void); 

#include <stdlib.h>


#endif 	    /* !_MEM_ALLOC_H_ */
