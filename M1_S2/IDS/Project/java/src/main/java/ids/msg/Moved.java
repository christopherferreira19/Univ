package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Moved implements Message {

    public Moved() {
    }

    public static Moved doDecode(MessageUnpacker unpacker) throws IOException {
        return new Moved();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.MOVED;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "Moved";
    }
}
