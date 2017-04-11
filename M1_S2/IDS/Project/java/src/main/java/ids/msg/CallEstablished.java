package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallEstablished implements Message {

    public CallEstablished() {
    }

    public static CallEstablished doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallEstablished();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_ESTABLISHED;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "Call established";
    }
}
