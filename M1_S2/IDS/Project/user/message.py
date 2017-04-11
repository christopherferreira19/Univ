import msgpack

class Message(object):
    USER_CODE = 0
    REGION_CODE = 1

    def code(self):
        return self.CODE

    def encode(self, user):
        packer = msgpack.Packer(autoreset = False, encoding = 'utf-8', use_bin_type = 1)
        packer.pack(Message.USER_CODE)
        packer.pack(user)
        packer.pack(self.code())
        self._doEncode(packer)
        return packer.bytes()

    @staticmethod
    def decode(bytes):
        unpacker = msgpack.Unpacker(use_list = True, encoding = 'utf-8')
        unpacker.feed(bytes)
        assert(unpacker.unpack() == Message.REGION_CODE)
        regionX = unpacker.unpack()
        regionY = unpacker.unpack()
        return MESSAGE_CLASSES_PER_CODE[unpacker.unpack()]._doDecode(unpacker)


class UserConnect(Message):
    CODE = 0
    def __init__(self, username):
        self.username = username
    def _doEncode(self, packer):
        packer.pack(self.username)
    @staticmethod
    def _doDecode(unpacker):
        return UserConnect(unpacker.unpack())

class UserDisconnect(Message):
    CODE = 1
    def __init__(self):
        pass
    def _doEncode(self, packer):
        pass

class CallRequest(Message):
    CODE = 7
    def __init__(self, nameTo):
        self.nameTo = nameTo
    def _doEncode(self, packer):
        packer.pack(self.nameTo)

class CallAcceptOk(Message):
    CODE = 8
    def __init__(self):
        pass
    def _doEncode(self, packer):
        pass

class CallAcceptKo(Message):
    CODE = 9
    def __init__(self):
        pass
    def _doEncode(self, packer):
        pass

class CallAccept(Message):
    CODE = 10
    def __init__(self):
        pass
    @staticmethod
    def _doDecode(unpacker):
        return CallAccept()

class CallUnavailable(Message):
    CODE = 11
    def __init__(self):
        pass
    def _doEncode(self, packer):
        pass
    @staticmethod
    def _doDecode(unpacker):
        return CallUnavailable()

class CallEstablished(Message):
    CODE = 12
    def __init__(self):
        pass
    @staticmethod
    def _doDecode(unpacker):
        return CallEstablished()

class CallSend(Message):
    CODE = 13
    def __init__(self, content):
        self.content = content
    def _doEncode(self, packer):
        packer.pack(self.content)
    def getContent(self):
        return self.content
    @staticmethod
    def _doDecode(unpacker):
        return CallSend(unpacker.unpack())

class CallReceive(Message):
    CODE = 14
    def __init__(self, userFrom, content):
        self.userFrom = userFrom
        self.content = content
    def getUserFrom(self):
        return self.userFrom
    def getContent(self):
        return self.content
    @staticmethod
    def _doDecode(unpacker):
        return CallReceive(unpacker.unpack(), unpacker.unpack())

class CallEnd(Message):
    CODE = 15
    def __init__(self):
        pass
    def _doEncode(self, packer):
        pass
    @staticmethod
    def _doDecode(unpacker):
        return CallEnd()

class Move(Message):
    CODE = 18
    def __init__(self, pos):
        self.x = pos[0];
        self.y = pos[1];
    def _doEncode(self, packer):
        packer.pack(self.x)
        packer.pack(self.y)
    @staticmethod
    def _doDecode(unpacker):
        return Move(unpacker.unpack(), unpacker.unpack())

class MoveOk(Message):
    CODE = 19
    def __init__(self, x, y):
        self.pos = (x, y)
    def getPosition(self):
        return self.pos
    @staticmethod
    def _doDecode(unpacker):
        return MoveOk(unpacker.unpack(), unpacker.unpack())

class MoveKo(Message):
    CODE = 20

    @staticmethod
    def _doDecode(unpacker):
        return MoveKo()

MESSAGE_CLASSES_PER_CODE = {
    kls.CODE: kls for kls in Message.__subclasses__()
}
