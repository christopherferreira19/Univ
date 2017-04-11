package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class Forward implements Message {

    private final int fromX;
    private final int fromY;
    private final String fromUser;
    private final int toX;
    private final int toY;
    private final String toUser;
    private final Message msg;


    public Forward(int fromX, int fromY, String fromUser, int toX, int toY, String toUser, Message msg) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromUser = fromUser;
        this.toX = toX;
        this.toY = toY;
        this.toUser = toUser;
        this.msg = msg;
    }

    public static Forward doDecode(MessageUnpacker unpacker) throws IOException {
        int fromX = unpacker.unpackInt();
        int fromY = unpacker.unpackInt();
        String fromUser = unpacker.unpackString();
        int toX = unpacker.unpackInt();
        int toY = unpacker.unpackInt();
        String toUser = unpacker.unpackString();

        Message msg = Message.decode(unpacker);
        return new Forward(fromX, fromY, fromUser, toX, toY, toUser, msg);
    }



    @Override
    public MessageCode getCode() {
        return MessageCode.FORWARD;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public String getFromUser() {
        return fromUser;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }

    public String getToUser() {
        return toUser;
    }

    public Message getMsg() {
        return msg;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packInt(fromX);
        packer.packInt(fromY);
        packer.packString(fromUser);
        packer.packInt(toX);
        packer.packInt(toY);
        packer.packString(toUser);
        msg.encode(packer);
    }

    @Override
    public String summary() {
        return String.format("Forward \"%s\" from (%d:%d \"%s\") to (%d:%d \"%s\")", msg.summary(),
                fromX, fromY, fromUser,
                toX, toY, toUser);
    }
}
