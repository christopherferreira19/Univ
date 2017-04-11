#ifndef TIME_WAIT_H
#define TIME_WAIT_H

#include "list.h"
#include "system.h"

class TimeWait
{
    public:
    TimeWait();
    ~TimeWait();
    void MapCarUpTo(long long time, VoidFunctionPtr func);
    void Register(long long time);
private:
    List *queue;
};

#endif
