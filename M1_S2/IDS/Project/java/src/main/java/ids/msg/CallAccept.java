package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallAccept implements Message {

    private final String username;

    public CallAccept(String username) {
        this.username = username;
    }

    public static CallAccept doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallAccept(unpacker.unpackString());
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_ACCEPT;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packString(username);
    }

    @Override
    public String summary() {
        return String.format("Call accept from %s ?", username);
    }
}
