#include "timewait.h"


TimeWait::TimeWait()
{
    queue = new List();
}

TimeWait::~TimeWait()
{
    delete queue;
}

void TimeWait::Register(long long time)
{
    IntStatus oldLevel = interrupt->SetLevel(IntOff);
    queue->SortedInsert((void*)currentThread, time);
    currentThread->Sleep();
    interrupt->SetLevel(oldLevel);
}

void TimeWait::MapCarUpTo(long long time, VoidFunctionPtr func)
{
    long long key;
    Thread* current = (Thread*) queue->SortedRemove(&key);
    if(current == NULL)
	return;
    while(key <= time && current != NULL)
    {
	func((int)current);
	current = (Thread*) queue->SortedRemove(&key);
    }
    if(key > time && current != NULL)
    {
	queue->SortedInsert((void*)current, key);	
    }
}
