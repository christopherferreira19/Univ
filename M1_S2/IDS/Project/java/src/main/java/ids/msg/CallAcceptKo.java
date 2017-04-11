package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class CallAcceptKo implements Message {

    public static CallAcceptKo doDecode(MessageUnpacker unpacker) throws IOException {
        return new CallAcceptKo();
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.CALL_ACCEPT_KO;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
    }

    @Override
    public String summary() {
        return "Call refused";
    }
}
