package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public interface Message {

    static Message decode(MessageUnpacker unpacker) {
        try {
            MessageCode messageCode = MessageCode.values()[unpacker.unpackInt()];
            switch (messageCode) {
                case USER_CONNECT: return UserConnect.doDecode(unpacker);
                case USER_DISCONNECT: return UserDisconnect.doDecode(unpacker);
                case USER_REGISTER: return UserRegister.doDecode(unpacker);
                case USER_UNREGISTER: return UserUnregister.doDecode(unpacker);
                case USER_LOOKUP: return UserLookup.doDecode(unpacker);
                case USER_FOUND: return UserFound.doDecode(unpacker);
                case USER_NOT_FOUND: return UserNotFound.doDecode(unpacker);

                case CALL_REQUEST: return CallRequest.doDecode(unpacker);
                case CALL_ACCEPT_OK: return CallAcceptOk.doDecode(unpacker);
                case CALL_ACCEPT_KO: return CallAcceptKo.doDecode(unpacker);
                case CALL_ACCEPT: return CallAccept.doDecode(unpacker);
                case CALL_UNAVAILABLE: return CallUnavailable.doDecode(unpacker);
                case CALL_ESTABLISHED: return CallEstablished.doDecode(unpacker);
                case CALL_SEND: return CallSend.doDecode(unpacker);
                case CALL_RECEIVE: return CallReceive.doDecode(unpacker);
                case CALL_END: return CallEnd.doDecode(unpacker);

                case FORWARD: return Forward.doDecode(unpacker);

                case MOVE: return Move.doDecode(unpacker);
                case MOVING: return Moving.doDecode(unpacker);
                case MOVED: return Moved.doDecode(unpacker);
            }

            throw new RuntimeException("Unknown Message Code : " + messageCode);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    MessageCode getCode();

    default void encode(MessagePacker packer) {
        try {
            packer.packInt(getCode().ordinal());
            doEncode(packer);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    void doEncode(MessagePacker packer) throws IOException;

    default String summary() {
        return getClass().getSimpleName();
    }
}
