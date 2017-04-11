#include "copyright.h"

#include "rtfm.h"

void
RingTest(int farAddr)
{
    const char *data = "Hell0 there!";
    char buffer[MaxRtfmPktSize];
    int maxIteration = 2;
    Rtfm *rtfmTo = new Rtfm(farAddr);
    Rtfm *rtfmFrom = new Rtfm((farAddr + 2) % 4);

    if(farAddr % 2)
    {
	printf("Waiting for connection\n");
	if(!rtfmTo->WaitForConnection())
    	{
    	    printf("Error opening connection\n");
    	    ASSERT(FALSE);
    	}
	Delay(2);
	printf("Connecting...\n");
	if(!rtfmFrom->Connect())
    	{
    	     printf("Error connection\n");
    	     ASSERT(FALSE);
        }
    }
    else
    {
	printf("Connecting...\n");
    	if(!rtfmFrom->Connect())
    	{
    	     printf("Error connection\n");
    	     ASSERT(FALSE);
        }
	Delay(2);
	printf("Waiting for connection\n");
	if(!rtfmTo->WaitForConnection())
    	{
    	    printf("Error opening connection\n");
    	    ASSERT(FALSE);
    	}
    }


    if(farAddr == 1)
    {
	printf("Initial message\n");
    	printf("Sending \"%s\"\n",data);
	if(!rtfmTo->Send(data))
	{
	    printf("Error when sending message \"%s\" to \"%d\"\n", data, farAddr);
	    ASSERT(FALSE);
	}
	maxIteration--;
    }
    
    int i;
    for(i=0;i<maxIteration;i++)
    {
    	rtfmFrom->Receive(buffer);
    	printf("Got \"%s\"\n",buffer);

    	buffer[4]++;
    	printf("Sending \"%s\"\n",buffer);
    	rtfmTo->Send(buffer);
    }
    
    if(farAddr == 1)
    {
	printf("last recv\n");
    	rtfmFrom->Receive(buffer);
    	printf("Got \"%s\"\n",buffer);
    }

    rtfmFrom->Close();
    rtfmTo->Close();
    
    interrupt->Halt();
}


