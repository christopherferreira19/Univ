package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserRegister implements Message {

    public static UserRegister doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserRegister(
                unpacker.unpackString(),
                unpacker.unpackInt(),
                unpacker.unpackInt());
    }

    private final String username;
    private final int x;
    private final int y;


    public UserRegister(String username, int x, int y) {
        this.username = username;
        this.x = x;
        this.y = y;
    }


    @Override
    public MessageCode getCode() {
        return MessageCode.USER_REGISTER;
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
        return "Register @" + username + " at region (" + x + ", " + y + ")";
    }
}
