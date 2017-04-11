package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class MoveOk implements Message {

    private final int x;
    private final int y;

    public MoveOk(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static MoveOk doDecode(MessageUnpacker unpacker) throws IOException {
        return new MoveOk(unpacker.unpackInt(), unpacker.unpackInt());
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.MOVE_OK;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packInt(x);
        packer.packInt(y);
    }

    @Override
    public String summary() {
        return String.format("Moved to (%d, %d) successfully", x, y);
    }
}
