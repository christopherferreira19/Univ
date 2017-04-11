package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserUnregister implements Message {

    public static UserUnregister doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserUnregister(
                unpacker.unpackString(),
                unpacker.unpackInt(),
                unpacker.unpackInt());
    }

    private final String username;
    private final int x;
    private final int y;


    public UserUnregister(String username, int x, int y) {
        this.username = username;
        this.x = x;
        this.y = y;
    }


    @Override
    public MessageCode getCode() {
        return MessageCode.USER_UNREGISTER;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(username);
        packer.packInt(x);
        packer.packInt(y);
    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String summary() {
        return "Unregister @" + username + " at region (" + x + ", " + y + ")";
    }
}
