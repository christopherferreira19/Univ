package ids.msg;

import ids.entity.UserState;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Moving implements Message {

    private final UserState state;

    public Moving(UserState state) {
        this.state = state;
    }

    public static Moving doDecode(MessageUnpacker unpacker) throws IOException {
        return new Moving(UserState.decode(unpacker));
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.MOVING;
    }

    public UserState getState() {
        return state;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        state.encode(packer);
    }

    @Override
    public String summary() {
        return String.format("Moving (State is [%s])", state.describe());
    }
}
