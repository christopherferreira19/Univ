#ifndef BIGFILETABLE_H
#define BIGFILETABLE_H

#include "disk.h"
#include "bitmap.h"

#define MaxBigFileSize      ((int) (NumDirect * BigFileTableSize * SectorSize))
#define BigFileTableSize    ((int) (SectorSize / sizeof(int)))


class BigFileTable {
  public:
    void FetchFrom(int sectorNumber);
    void WriteBack(int sectorNumber);

    void Allocate(BitMap *freeMap, int numSectors);
    void Deallocate(BitMap *freeMap);

    int dataTable[BigFileTableSize];

};

#endif // BIGFILETABLE_H
