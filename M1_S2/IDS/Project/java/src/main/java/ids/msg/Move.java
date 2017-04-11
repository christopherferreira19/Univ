package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Move implements Message {

    private final int x;
    private final int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Move doDecode(MessageUnpacker unpacker) throws IOException {
        return new Move(unpacker.unpackInt(), unpacker.unpackInt());
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.MOVE;
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
        return String.format("Move to (%d, %d)", x, y);
    }
}
