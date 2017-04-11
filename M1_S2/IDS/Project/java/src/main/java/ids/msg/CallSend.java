package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallSend implements Message {

    public CallSend(String content) {
        this.content = content;
    }

    public static CallSend doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallSend(unpacker.unpackString());
    }

    private final String content;

    public String getContent() {
        return content;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_SEND;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(content);
    }

    @Override
    public String summary() {
        return String.format("\"%s\"", content);
    }
}
