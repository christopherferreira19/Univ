import message

class User:

    def __init__(self, name, pos, channel):
        self.name = name
        self.pos = pos
        self.channel = channel
        self.region = "Region#(" + ",".join(str(x) for x in pos) + ")"
        
    def Display(self):
        print("User " + self.name + " in " + ",".join(str(x) for x in self.pos))

    def Send(self, msg):
        print("Sending", msg) 
        
    def Connect(self):
        msg = message.UserConnect(self.name)
        encoded = msg.encode("User#" + self.name)
        self.Send(encoded)
        self.Display()
        print("is connecting to" +  self.region)
        

    def Call(self, nameToCall):
        msg = message.CallRequest(self.name, nameToCall)
        self.Display()
        print("is calling " +  self.region)

    def Receive(self, msg):
        print("Received ", msg)
