#ifndef __ATOMIC_H__
#define __ATOMIC_H__

#define ATOMIC_MEMORY_MODEL __ATOMIC_SEQ_CST

#define fetch_and_add(ptr, val) __atomic_fetch_add((ptr), (val), ATOMIC_MEMORY_MODEL);

#endif