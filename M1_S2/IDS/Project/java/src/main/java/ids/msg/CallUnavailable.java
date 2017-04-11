package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.nio.ByteBuffer;

public class CallUnavailable implements Message {

    public static CallUnavailable doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallUnavailable();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_UNAVAILABLE;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }
}
