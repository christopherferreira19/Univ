package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallAcceptOk implements Message {

    public CallAcceptOk() {
    }

    public static CallAcceptOk doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallAcceptOk();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_ACCEPT_OK;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "Call accepted";
    }
}
