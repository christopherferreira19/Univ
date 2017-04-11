#include "frameprovider.h"
#include "system.h"
#include <strings.h>

FrameProvider::FrameProvider() {
    DEBUG ('a', "Initializing frame provider, num pages %d\n", NumPhysPages);
    bitmap = new BitMap(NumPhysPages);
}

FrameProvider::~FrameProvider() {
    delete bitmap;
}

unsigned int FrameProvider::NumAvailFrame() {
    return bitmap->NumClear();
}

unsigned int FrameProvider::GetEmptyFrame() {
    int frame = bitmap->Find();
    bitmap->Mark(frame);
    bzero(machine->mainMemory + frame * PageSize, PageSize);
    return frame;
}

void FrameProvider::ReleaseFrame(unsigned int frame) {
    bitmap->Clear(frame);
}


