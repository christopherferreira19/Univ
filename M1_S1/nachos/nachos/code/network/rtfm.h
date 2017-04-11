// Reliable Transfer of Fixed-size Message

#ifdef STEP6


#ifndef RTFM_H
#define RTFM_H

#include "timewait.h"
#include "system.h"

#define MAXREEMISSIONS 100
#define TEMPO 2 * NetworkTime


enum MsgType {
    MsgData,
    MsgConnect,
    MsgConnectAck,
    MsgAck,
    MsgClose,
    MsgAbort
};

typedef int seqId_t;

class RtfmPktHeader {
  public:
    seqId_t seqId;
    MsgType type;
    unsigned length;
};

#define MaxRtfmPktSize 	(MaxMailSize - sizeof (RtfmPktHeader))

class RtfmPkt {
  public:
     RtfmPkt(RtfmPktHeader header_arg, char *msgData);

     RtfmPktHeader header;	// Header
     char data[MaxRtfmPktSize];	// Payload -- message data
};

class Rtfm {
  public:
    Rtfm(int farAddr);
    ~Rtfm();
    bool WaitForConnection();
    bool Connect();
    bool Send(const char *data);
    void Receive(char *data);
    void Close();
    void getMsg();
  private:
    NetworkAddress addr;
    Lock *ackLock;
    Semaphore *semAck;
    Semaphore *semRcv;
    Semaphore *semConnect;
    Thread *ackThread;
    Thread *rcvThread;

    RtfmPkt *currentPkt;
    
    int msgBox;
    int outMsgBox;
    unsigned int tempo;
    
    seqId_t seqId;
    seqId_t ackId;
    seqId_t outAckId;

    bool isConnected;
    bool isClosed;
    bool waitingForCo;
    bool abort;
    
    bool do_Connect(int ack_arg, MsgType msgType, int outBox);
    bool Do_Send(const char *data, MsgType msgTypex);
    bool Do_Send(const char *data, MsgType msgTypex, int outBox2);
    void SendAck(PacketHeader inPktHdr, MailHeader inMailHdr, seqId_t ackNb);
    void SendMsgAbort(PacketHeader inPktHdr, MailHeader inMailHdr);
    void wait(int time);

    void ProcessConnect(RtfmPkt *rtfmPkt, MailHeader inMailHdr);
    void ProcessConnectAck(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr);
    void ProcessAck(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr);
    void ProcessMsg(RtfmPkt *rtfmPkt, PacketHeader inPktHdr, MailHeader inMailHdr);
    void ProcessClose(RtfmPkt *rtfmPkt);

    void PrintHeader(RtfmPkt *rtfmPkt);
};

#endif // RTFM_H



#endif
