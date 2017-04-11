package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserDisconnect implements Message {

    public UserDisconnect() {
    }

    public static UserDisconnect doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserDisconnect();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.USER_DISCONNECT;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "Disconnect me";
    }
}
