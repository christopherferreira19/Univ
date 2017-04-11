#ifndef FRAME_PROVIDER_H
#define FRAME_PROVIDER_H

#include "bitmap.h"

class FrameProvider {
public:
    FrameProvider();
    ~FrameProvider();
    unsigned int NumAvailFrame();
    unsigned int GetEmptyFrame();
    void ReleaseFrame(unsigned int frame);
private:
    BitMap* bitmap;
};

#endif