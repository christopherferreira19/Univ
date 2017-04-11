#ifndef __USERFILES_H__
#define __USERFILES_H__

#include "openfile.h"

class RWLock;

typedef int nfid_t;

typedef enum {
	FILE_READ       = 1 << 0,
	FILE_WRITE      = 1 << 1,
	FILE_READ_WRITE = FILE_READ | FILE_WRITE,
} FileOpenMode;

typedef struct {
    nfid_t nfid;
    FileOpenMode mode;
    OpenFile* openFile;
    RWLock* lock;
} OpenFileEntry;

class OpenFiles {
public:
    OpenFiles();
    ~OpenFiles();

    nfid_t Open(char *name, FileOpenMode mode);
    bool Seek(nfid_t nfid, int position);
    int Read(nfid_t nfid, char *buffer, int size);
    int Write(nfid_t nfid, char *buffer, int size);
    void Close(nfid_t nfid);
private:
    nfid_t nextNfid;
    OpenFileEntry* array;
    OpenFileEntry* get(nfid_t nfid);
};

#endif