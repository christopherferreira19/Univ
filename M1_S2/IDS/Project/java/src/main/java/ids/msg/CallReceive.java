package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallReceive implements Message {

    private final String username;
    private final String content;

    public CallReceive(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public static CallReceive doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallReceive(unpacker.unpackString(), unpacker.unpackString());
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_RECEIVE;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(username);
        packer.packString(content);
    }

    @Override
    public String summary() {
        return String.format("\"%s\"", content);
    }
}
