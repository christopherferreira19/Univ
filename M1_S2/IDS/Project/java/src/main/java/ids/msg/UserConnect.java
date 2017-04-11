package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserConnect implements Message {

    public static UserConnect doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserConnect(unpacker.unpackString());
    }

    private final String username;

    public UserConnect(String username) {
        this.username = username;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.USER_CONNECT;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(username);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String summary() {
        return "@" + username + " wants to connect";
    }
}
