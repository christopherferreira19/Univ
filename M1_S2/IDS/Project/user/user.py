from threading import Thread, Lock, Semaphore
import message

def consumer(owner):
    channel = owner.channel
    channel.start_consuming()

class User:

    def __init__(self, name, pos, channel):
        self.name = name
        self.channel = channel
        self.lock = Lock()
        self.semCallInit = Semaphore(0)
        self.semOnMove = Semaphore(0)
        self.queue_name = "User#" + name
        channel.queue_declare(queue=self.queue_name)
        channel.basic_consume(self.Receive,
                      queue=self.queue_name,
                      no_ack=True)
        self.thread = Thread(target = consumer, args = (self,))
        self.thread.daemon = True
        self.SetRegion(pos)
        self.Init()
        self.start()

    def Init(self):
        self.acceptCall = False
        self.onCall = False
        self.tryingCall = False
        self.connected = False

    def Display(self):
        print("User " + self.name + " in " + ",".join(str(x) for x in self.pos))

    def Send(self, msg):
        encoded = msg.encode(self.name)
        self.channel.basic_publish(exchange='',
                      routing_key=self.region,
                                   body=encoded)

    def SetRegion(self, pos):
        self.region = "Region#(" + ",".join(str(x) for x in pos) + ")"

    def ConnectTo(self, pos):
        self.SetRegion(pos)
        self.Connect()

    def Connect(self):
        if(self.connected == True):
            print("Already connected")
            return
        msg = message.UserConnect(self.name)
        self.Send(msg)
        with self.lock:
            self.acceptCall = True
        self.connected = True

    def Call(self, nameToCall):
        with self.lock:
            onCall = self.onCall
            tryingCall = self.tryingCall

        if(onCall == False and tryingCall == False):
            with self.lock:
                self.tryingCall = True
            msg = message.CallRequest(nameToCall)
            self.Send(msg)
            self.semCallInit.acquire()
            with self.lock:
                if(self.onCall == True):
                    print("call established")
                else:
                    print("error, call aborted")

        elif(onCall == True):
            print ("cannot make another call when already on call")

        elif(tryingCall == True):
            print ("cannot try to make another call when already trying")

    def Say(self, words):
        with self.lock:
            if(self.onCall == False):
                print("Error, the user is not on call")
        msg = message.CallSend(words)
        self.Send(msg)

    def Disconnect(self):
        if(self.connected == False):
            print("Cannot disconnect if not already connected")
            return
        msg = message.UserDisconnect()
        self.Send(msg)
        self.Init()

    def Move(self, pos):
        with self.lock:
            if(self.connected == False):
                print("Cannot move if not already connected")
                return
        msg = message.Move(pos)
        self.Send(msg)
        self.semOnMove.acquire()


    def EndCall(self):
        with self.lock:
            if(self.onCall == False):
                print("Can only end an ongoing call")
                return
            self.onCall = False
            self.acceptCall = True
        msg = message.CallEnd()
        self.Send(msg)

    def doCallUnavailable(self, msg):
        with self.lock:
            if(self.tryingCall == True):
                self.tryingCall = False
                self.semCallInit.release()

    def doCallEnd(self, msg):
        with self.lock:
            if(self.onCall == False):
                return
            self.onCall = False
            self.acceptCall = True
        print("Call has ended")

    def doCallAccept(self, msg):
        with self.lock:
            acceptCall = self.acceptCall
        if(acceptCall == True):
            answer = message.CallAcceptOk()
            with self.lock:
                self.onCall = True
            print("Call established")
        else:
            answer = message.CallAcceptKo()
        self.Send(answer)

    def doCallEstablished(self, msg):
        with self.lock:
            if(self.tryingCall == True):
                self.acceptCall = False
                self.onCall = True
                self.tryingCall = False
                self.semCallInit.release()

    def doCallReceive(self, msg):
        with self.lock:
            if(self.tryingCall == True):
                self.acceptCall = False
                self.onCall = True
                self.tryingCall = False
                self.semCallInit.release()
        print("@" + msg.userFrom + ":" + msg.getContent())

    def doMoveOk(self, msg):
        with self.lock:
            self.SetRegion(msg.getPosition())
            self.connected = True
        self.semOnMove.release()

    def doMoveKo(self, msg):
        with self.lock:
            self.Init()
        self.semOnMove.release()


    def Receive(self, ch, method, properties, body):
        msg = message.Message.decode(body)
        if isinstance(msg, message.CallEstablished):
            self.doCallEstablished(msg)
        if isinstance(msg, message.CallAccept):
            self.doCallAccept(msg)
        if isinstance(msg, message.CallEnd):
            self.doCallEnd(msg)
        if isinstance(msg, message.CallReceive):
            self.doCallReceive(msg)
        if isinstance(msg, message.CallUnavailable):
            self.doCallUnavailable(msg)
        if isinstance(msg, message.MoveOk):
            self.doMoveOk(msg)
        if isinstance(msg, message.MoveKo):
            self.doMoveKo(msg)

    def start(self):
        try:
            self.thread.start()
        except (KeyboardInterrupt, SystemExit):
            cleanup_stop_thread()
            sys.exit()

    def join(self):
        self.thread.join()
