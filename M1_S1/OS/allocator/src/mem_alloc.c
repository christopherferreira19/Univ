#include "mem_alloc.h"
#include <stdio.h>
#include <assert.h>
#include <string.h>

#include "mem_alloc_types.h"

/* memory */
char memory[MEMORY_SIZE];

/* Pointer to the first free block in the memory */
mem_free_block_t *first_free; 


#define ULONG(x)((long unsigned int)(x))
#define max(x,y) (x>y?x:y)

#if defined(FIRST_FIT)

/* code specific to first fit strategy can be inserted here */

#elif defined(BEST_FIT)

/* code specific to best fit strategy can be inserted here */

#elif defined(WORST_FIT)

/* code specific to worst fit strategy can be inserted here */

#endif

void run_at_exit(void)
{
    /* function called when the programs exits */
    /* To be used to display memory leaks informations */
    
    /* ... */
}



void memory_init(void){

    /* register the function that will be called when the programs exits*/
    atexit(run_at_exit);

    first_free = (mem_free_block_t*) memory;
    first_free->size = MEMORY_SIZE;
    first_free->next = NULL;
}

char *memory_alloc(int size){
    int block_size = sizeof(mem_free_block_t) + size;
    
    mem_free_block_t * it = first_free;    
    mem_free_block_t ** it_ref = &first_free;
        
    while(it != NULL) {
      int remaining_size = it->size - block_size;
      if (remaining_size >= 0) {
        if (remaining_size <= sizeof(mem_free_block_t)) {
          // If remaining size is not enough to store metadata,
          // just include it to the allocated block
          block_size = it->size;
        }
        
        // Let's consider padding for alignement
        block_size += 8 - block_size % 8;
        if (block_size > it->size) {
          it_ref = &(it->next);
          it = it->next;  
          continue;
        }

        if (it->size == block_size) { // Occupies full block
          *it_ref = it->next;
        }
        else {  // Requires creation of a new free block
          mem_free_block_t* new_block;
          new_block = (mem_free_block_t*) ((char*) it + block_size);
          new_block->size = remaining_size;
          new_block->next = it->next;
          *it_ref = new_block;
        }

        return ((char*) it) + sizeof(mem_used_block_t);
      }

      it_ref = &(it->next);
      it = it->next;    
    }

    printf("Out of Memory Error.\n");
    return NULL;
}

void memory_free(char *p){
    p -= sizeof(mem_used_block_t);
    mem_free_block_t* block = (mem_free_block_t*) p;
    
    char* p2 = p + block->size;
    
    mem_free_block_t * it = first_free;    
    mem_free_block_t * it_ref = first_free;
    
    while(((char*)it < p2) && (it != NULL)) {
      it_ref = it;
      it = it->next;  
    } 
    
    if(p2 == (char*)it) {
      block->size += it->size;
      block->next = it->next;
      it_ref->next = block;
    } 
    else {
      block->next = it;
      it_ref->next = block;
    }
}


void print_alloc_info(char *addr, int size){
  if(addr){
      fprintf(stderr, "ALLOC at : %lu (%d byte(s))\n", 
              ULONG(addr - memory), size);
  }
  else{
      fprintf(stderr, "Warning, system is out of memory\n"); 
  }
}


void print_free_info(char *addr){
    if(addr){
        fprintf(stderr, "FREE  at : %lu \n", ULONG(addr - memory));
    }
    else{
        fprintf(stderr, "FREE  at : %lu \n", ULONG(0));
    }
}

void print_error_alloc(int size) 
{
    fprintf(stderr, "ALLOC error : can't allocate %d bytes\n", size);
}

void print_info(void) {
  fprintf(stderr, "Memory : [%lu %lu] (%lu bytes)\n", (long unsigned int) memory, (long unsigned int) (memory+MEMORY_SIZE), (long unsigned int) (MEMORY_SIZE));
}


void print_free_blocks(void) {
    mem_free_block_t *current; 
    fprintf(stderr, "Begin of free block list :\n"); 
    for(current = first_free; current != NULL; current = current->next)
        fprintf(stderr, "Free block at address %lu, size %u\n", ULONG((char*)current - memory), current->size);
}

char *heap_base(void) {
  return memory;
}

#ifdef MAIN
int main(int argc, char **argv){
  /* The main can be changed, it is *not* involved in tests */
  memory_init();
  print_info(); 
  print_free_blocks();
  int i ; 
  for( i = 0; i < 10; i++){
    char *b = memory_alloc(rand()%8);
    memory_free(b); 
    print_free_blocks();
  }

  char * a = memory_alloc(15);
  //a=realloc(a, 20); 
  memory_free(a);


  a = memory_alloc(10);
  memory_free(a);

  fprintf(stderr,"%lu\n",(long unsigned int) (memory_alloc(9)));
  return EXIT_SUCCESS;
}
#endif 
