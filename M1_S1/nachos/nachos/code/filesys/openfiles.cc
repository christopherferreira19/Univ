#include "openfiles.h"
#include "system.h"
#include "synch.h"

#define MAX_USER_FILES 10

static inline int NFID_TO_INDEX(nfid_t nfid) {
    return nfid % MAX_USER_FILES;
}

OpenFiles::OpenFiles() {
    nextNfid = 0;
    array = new OpenFileEntry[MAX_USER_FILES];
    for (int i = 0; i < MAX_USER_FILES; i++) {
        array[i].nfid = -1;
        array[i].lock = new RWLock("OpenFile");
    }
}

OpenFiles::~OpenFiles() {
    for (int i = 0; i < MAX_USER_FILES; i++) {
        delete array[i].lock;
    }

    delete [] array;
}

nfid_t
OpenFiles::Open(char *name, FileOpenMode mode) {
    nfid_t nfid = nextNfid;
    while (array[NFID_TO_INDEX(nextNfid)].nfid >= 0) {
        nfid++;
    }
    
    OpenFile* openFile = fileSystem->Open(name);
    if (openFile == NULL) {
        return -1;
    }

    array[NFID_TO_INDEX(nextNfid)].nfid = nfid;
    array[NFID_TO_INDEX(nextNfid)].mode = mode;
    array[NFID_TO_INDEX(nextNfid)].openFile = openFile;

    nextNfid = nfid + 1;
    return nfid;
}

OpenFileEntry*
OpenFiles::get(nfid_t nfid) {
    if (nfid < 0) { return NULL; }
    OpenFileEntry* entry = &array[NFID_TO_INDEX(nfid)];
    if (entry->nfid != nfid) { return NULL; }
    return entry;
}

bool
OpenFiles::Seek(nfid_t nfid, int position) {
    OpenFileEntry* entry = get(nfid);
    if (entry == NULL) { return false; }

    entry->lock->AcquireRead();
    if (entry->openFile == NULL) {
        return false;
    }

    entry->openFile->Seek(position);
    entry->lock->Release();
    return true;
}

int
OpenFiles::Read(nfid_t nfid, char *buffer, int size) {
    OpenFileEntry* entry = get(nfid);
    if (entry == NULL) { return -1; }
    if ((entry->mode & FILE_READ) == 0) { return -2; }

    entry->lock->AcquireRead();
    if (entry->openFile == NULL) {
        return -3;
    }

    int result = entry->openFile->Read(buffer, size);
    entry->lock->Release();
    return result;
}

int
OpenFiles::Write(nfid_t nfid, char *buffer, int size) {
    OpenFileEntry* entry = get(nfid);
    if (entry == NULL) { return -1; }
    if ((entry->mode & FILE_WRITE) == 0) { return -2; }

    entry->lock->AcquireWrite();
    if (entry->openFile == NULL) {
        return -3;
    }

    int result = entry->openFile->Write(buffer, size);
    entry->lock->Release();
    return result;
}

void
OpenFiles::Close(nfid_t nfid) {
    OpenFileEntry* entry = get(nfid);
    if (entry == NULL) {
        return;
    }

    entry->lock->AcquireWrite();
    array[NFID_TO_INDEX(nfid)].nfid = -1;
    delete entry->openFile;
    entry->lock->Release();
}