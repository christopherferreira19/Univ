// Reliable Transfer of Fixed-size Message

#include "rtfm.h"

void static processRcv(int arg)
{
    ((Rtfm*)arg)->getMsg();
}

RtfmPkt::RtfmPkt(RtfmPktHeader header_arg, char *msgData)
{
    ASSERT(header_arg.length <= MaxRtfmPktSize);
    header = header_arg;
    bcopy(msgData, data, MaxRtfmPktSize);
}

Rtfm::Rtfm(int farAddr)
{
    addr = farAddr;
    ackLock = new Lock("ack lock");
    semAck = new Semaphore("ack sem", 0);
    semRcv = new Semaphore("rcv sem", 0);
    semConnect = new Semaphore("connect sem", 0);

    ackId = -1;
    isConnected = FALSE;
    isClosed = FALSE;
    waitingForCo = FALSE;
    tempo = TEMPO;
    
    msgBox = postOffice->getFreeBox();
    outMsgBox = -1;
    
    int i;
    seqId = Random();
    for(i=0;i<farAddr+msgBox;i++)
	seqId = Random();

    DEBUG('x', "MesagegBox %d farAddr %d\n", msgBox, farAddr);
    
    rcvThread = new Thread("recieve manager");
    rcvThread->Fork(processRcv, (int)this);

}

Rtfm::~Rtfm()
{
    isConnected = FALSE;
    isClosed = TRUE;
    delete ackLock;
    delete semAck;
    delete semRcv;
    if(currentPkt != NULL)
    {
	delete currentPkt;
    }

    postOffice->FreeBox(msgBox);
}

bool Rtfm::WaitForConnection()
{
    DEBUG('x', "Waiting for connection on box %d\n", msgBox);
    if(isConnected)
	return FALSE;
    if(msgBox < 0)
	return FALSE;
    
    waitingForCo = TRUE;
    semConnect->P();
    waitingForCo = FALSE;

    DEBUG('x', "Estabishing connection on box :: %d\n", outMsgBox);
    isConnected = do_Connect(outAckId, MsgConnectAck, outMsgBox);
    
    DEBUG('x', "seqId %d ackId %d outAckId %d\n", seqId, ackId, outAckId);
    DEBUG('x', "isConnected %d\n", isConnected);
    return isConnected;
}

bool Rtfm::Connect()
{
    DEBUG('x', "Connecting\n");
    if(isConnected)
	return FALSE;
    if(msgBox < 0)
	return FALSE;

    outAckId = -1;
    int i;
    for(i=0;i<postOffice->getNumBoxes();i++)
    {
	isConnected = do_Connect(-1, MsgConnect, i);
	if(isConnected)
	{
	    break;
	}
	wait(tempo);
    }

    DEBUG('x', "seqId %d ackId %d outAckId %d\n", seqId, ackId, outAckId);
    DEBUG('x', "isConnected %d\n", isConnected);
    return isConnected;
    
}

bool Rtfm::do_Connect(int ack_arg, MsgType msgType, int outBox)
{
    PacketHeader outPktHdr;
    MailHeader outMailHdr;
    unsigned int reemisson = 0;	

    int size = sizeof(RtfmPktHeader);
    char *buffer = new char[size];
	
    RtfmPktHeader header;
    ackId = seqId;
    header.seqId = seqId++;
    header.type = msgType;
    header.length = 0;
    bcopy(&header, buffer, size);
	
    outPktHdr.to = addr;
    outMailHdr.to = outBox;
    outMailHdr.from = msgBox;
    outMailHdr.length = size;
	
    DEBUG('x', "%p do_connect with \"%d\", \"%d\" on box %d from %d to addr %d\n",
	  this, outAckId, ack_arg, outBox, msgBox, addr);	

    abort = FALSE;
    while( (ackId != seqId) && (outAckId == ack_arg) && (reemisson != MAXREEMISSIONS) )
    {
	if(abort == TRUE)
	{
	    DEBUG('x', "ABORTING\n");
	    abort = FALSE;
	    return FALSE;
	}
	postOffice->Send(outPktHdr, outMailHdr, buffer);
	reemisson++;
	// tempo = (tempo < TEMPO * reemisson) ? TEMPO * reemisson : tempo;
	wait(tempo);
    }

    delete [] buffer;
    
    if(reemisson != MAXREEMISSIONS)
	return TRUE;
    return FALSE;
}

bool Rtfm::Send(const char *data)
{
    return Do_Send(data, MsgData);
}

bool Rtfm::Do_Send(const char *data, MsgType msgType)
{
    return Do_Send(data, msgType, outMsgBox);
}

bool Rtfm::Do_Send(const char *data, MsgType msgType, int outBox)
{
    if(isClosed)
    {
    	DEBUG('x', "Cannot send, connection is closed\n");
    	return FALSE;
    }

    DEBUG('x', "OutMsgBox %d\n", outMsgBox);
    ASSERT(outMsgBox > -1);
    
    PacketHeader outPktHdr;
    MailHeader outMailHdr;
    int reemisson = 0;	

    char *buffer = new char[MaxMailSize];

    RtfmPktHeader header;
    header.seqId = seqId++;
    header.type = msgType;
    if(data != NULL)
    {
	header.length = strlen(data) + 1;
	bcopy(data, buffer + sizeof(RtfmPktHeader), header.length);
    }
    else
    {
	header.length = 0;
    }
    bcopy(&header, buffer, sizeof(RtfmPktHeader));


    DEBUG('x', "Sending \"%s\"\n", buffer + sizeof(RtfmPktHeader));
    // DEBUG('x', "with seqId\"%d\",  current ack %d outAck %d\n", header.seqId, ackId, outAckId);
	
    outPktHdr.to = addr;
    outMailHdr.to = outBox;
    outMailHdr.from = msgBox;
    outMailHdr.length = header.length + sizeof(RtfmPktHeader);
	
    while( (ackId != seqId) && (reemisson != MAXREEMISSIONS) )
    {
	postOffice->Send(outPktHdr, outMailHdr, buffer);
	reemisson++;
	wait(tempo);
    }
    DEBUG('x', "Send done %d\n", reemisson);
    DEBUG('x', "with seqId\"%d\",  current ack %d outAck %d\n", seqId, ackId, outAckId);
    delete [] buffer;
    if(ackId == seqId)
	return TRUE;
    return FALSE;
}

void Rtfm::getMsg()
{
    PacketHeader inPktHdr;
    MailHeader inMailHdr;
    RtfmPkt *rtfmPkt;
    char buffer[MaxMailSize];

    while(!isClosed)
    {
	postOffice->Receive(msgBox, &inPktHdr, &inMailHdr, buffer);
	rtfmPkt = (RtfmPkt*) buffer;
	if (DebugIsEnabled('x')) {
	    printf("%p Got rtfm packet on box: %d\n", this, msgBox);
	    PrintHeader(rtfmPkt);
	}
	if(!isConnected)
	{
	    switch (rtfmPkt->header.type)
	    {
	    case MsgConnect:
		ASSERT(rtfmPkt->header.length == 0);
		ProcessConnect(rtfmPkt, inMailHdr);
		break;
	    case MsgConnectAck:
		ASSERT(rtfmPkt->header.length == 0);
		ProcessConnectAck(rtfmPkt, inPktHdr, inMailHdr);
		break;
	    case MsgAck:
		ProcessAck(rtfmPkt, inPktHdr, inMailHdr);
		break;	    
	    case MsgAbort:
		abort = TRUE;
		break;
	    default:
		SendMsgAbort(inPktHdr, inMailHdr);
		break;
	    }
	}

	if(isConnected)
	{
	    postOffice->Receive(msgBox, &inPktHdr, &inMailHdr, buffer);
	    rtfmPkt = (RtfmPkt*) buffer;
	    switch (rtfmPkt->header.type)
	    {
	    case MsgData:
		ProcessMsg(rtfmPkt, inPktHdr, inMailHdr);
		break;
	    case MsgAck:
		ProcessAck(rtfmPkt, inPktHdr, inMailHdr);
		break;	    
	    case MsgClose:
		ProcessClose(rtfmPkt);
		break;
	    case MsgConnect:
		SendMsgAbort(inPktHdr, inMailHdr);
		break;
	    case MsgConnectAck:
		ASSERT(rtfmPkt->header.length == 0);
		ProcessConnectAck(rtfmPkt, inPktHdr, inMailHdr);
		break;
	    case MsgAbort:
		abort = TRUE;
		break;
	    default: break;
	    }
	}
    }
}

void Rtfm::ProcessConnect(RtfmPkt *rtfmPkt, MailHeader inMailHdr)
{
    ASSERT(!isConnected);
    if(rtfmPkt->header.length != 0)
    {
	return;
    }
    if(waitingForCo)
    {
	ackLock->Acquire();
	outAckId = rtfmPkt->header.seqId + 1;
	outMsgBox = inMailHdr.from;
	ackLock->Release();
	DEBUG('x', "process connect outAckId %d from %d\n", outAckId, inMailHdr.from);
	semConnect->V();
    }
}

void Rtfm::ProcessConnectAck(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr)
{
    if(isConnected && outAckId == rtfmPkt->header.seqId + 1)
    {
	SendAck(inPktHdr, inMailHdr, outAckId);
	return;
    }
    else if(!isConnected)
    {
	ackLock->Acquire();
	outAckId = rtfmPkt->header.seqId + 1;
	outMsgBox = inMailHdr.from;
	ackLock->Release();
	DEBUG('x', "process ack connect outAckId %d\n", outAckId);
	SendAck(inPktHdr, inMailHdr, outAckId);
    }
}

void Rtfm::ProcessAck(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr)
{
    ackLock->Acquire();
    ackId = rtfmPkt->header.seqId;
    ackLock->Release();
    DEBUG('x', "ack recieved %d\n", ackId);
}

void Rtfm::ProcessClose(RtfmPkt *rtfmPkt)
{
    isClosed = TRUE;
    Do_Send(NULL, MsgClose);
}

void Rtfm::ProcessMsg(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr)
{
    	DEBUG('x', "got msg\n");
    	if(rtfmPkt->header.seqId == outAckId)
    	{
    	    outAckId++;
    	    currentPkt = new RtfmPkt(rtfmPkt->header, rtfmPkt->data);
	    DEBUG('x', "%p keep msg!\n", this);
	    semRcv->V();
    	}
	else
	{
	    DEBUG('x', "%p discard msg! %d %d\n", this, rtfmPkt->header.seqId, outAckId);
	}
	SendAck(inPktHdr, inMailHdr, outAckId);
    
}

void Rtfm::Receive(char *data)
{
    DEBUG('x', "%p Reciev..", this);
    semRcv->P();
    ASSERT(currentPkt != NULL);
    bcopy(currentPkt->data, data, currentPkt->header.length);
    DEBUG('x', "Reciev end\n");
    delete currentPkt;

}

void Rtfm::SendAck(PacketHeader inPktHdr, MailHeader inMailHdr, seqId_t ackNb)
{
    RtfmPktHeader header;
    PacketHeader outPktHdr;
    MailHeader outMailHdr;

    header.length = 0;
    header.seqId = ackNb;
    header.type  = MsgAck;
    DEBUG('x', "sending ack %d to %d\n", ackNb, inMailHdr.from);
    outPktHdr.to = inPktHdr.from;
    outMailHdr.to = inMailHdr.from;
    outMailHdr.length = sizeof(RtfmPktHeader);
    postOffice->Send(outPktHdr, outMailHdr, (char*)&header);

}

void Rtfm::Close()
{
    if(!isClosed)
	Do_Send(NULL, MsgClose);

    wait(tempo * MAXREEMISSIONS);
    isClosed = TRUE;
}

void Rtfm::SendMsgAbort(PacketHeader inPktHdr, MailHeader inMailHdr)
{
    RtfmPktHeader header;
    PacketHeader outPktHdr;
    MailHeader outMailHdr;

    header.length = 0;
    header.seqId = -2;
    header.type  = MsgAbort;
    outPktHdr.to = inPktHdr.from;
    outMailHdr.to = inMailHdr.from;
    outMailHdr.length = sizeof(RtfmPktHeader);
    postOffice->Send(outPktHdr, outMailHdr, (char*)&header);

}

void Rtfm::wait(int time)
{
    ASSERT(time >= 0);
    timeWait->Register(stats->totalTicks + time);
}

void
Rtfm::PrintHeader(RtfmPkt *rtfmPkt)
{
    printf("- seqId: %d\n", rtfmPkt->header.seqId);
    printf("- type: %d\n", rtfmPkt->header.type);
    printf("- length: %d\n", rtfmPkt->header.length);
}
