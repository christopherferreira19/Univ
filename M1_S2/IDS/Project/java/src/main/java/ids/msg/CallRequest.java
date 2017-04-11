package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallRequest implements Message {

    public static CallRequest doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallRequest(unpacker.unpackString());
    }

    private final String callee;

    public CallRequest(String callee) {
        this.callee = callee;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_REQUEST;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(callee);
    }

    public String getCallee() {
        return callee;
    }

    @Override
    public String summary() {
        return String.format("I want to call @%s", callee);
    }
}
