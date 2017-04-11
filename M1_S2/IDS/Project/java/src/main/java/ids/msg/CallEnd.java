package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallEnd implements Message {

    public CallEnd() {
    }

    public static CallEnd doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallEnd();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_END;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "End my call";
    }
}
