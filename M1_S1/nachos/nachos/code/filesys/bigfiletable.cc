#include "system.h"
#include "bigfiletable.h"


void
BigFileTable::FetchFrom(int sector)
{
    synchDisk->ReadSector(sector, (char *)this);
}


void
BigFileTable::WriteBack(int sector)
{
    synchDisk->WriteSector(sector, (char *)this); 
}


void
BigFileTable::Allocate(BitMap *freeMap, int numSectors)
{
    for (int i = 0; i < BigFileTableSize; i++) {
        dataTable[i] = i < numSectors ? freeMap->Find() : -1;
    }
}

void BigFileTable::Deallocate(BitMap *freeMap)
{
    for (int i = 0; i < BigFileTableSize && dataTable[i] >= 0; i++) {
        freeMap->Clear(dataTable[i]);
    }
}
