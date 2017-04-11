package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserNotFound implements Message {

    private final int replyToX;
    private final int replyToY;
    private final String replyToUser;

    public static UserNotFound doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserNotFound(
                unpacker.unpackInt(),
                unpacker.unpackInt(),
                unpacker.unpackString());
    }

    public UserNotFound(int replyToX, int replyToY, String replyToUser) {
        this.replyToX = replyToX;
        this.replyToY = replyToY;
        this.replyToUser = replyToUser;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.USER_NOT_FOUND;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packInt(replyToX);
        packer.packInt(replyToY);
        packer.packString(replyToUser);
    }

    public int getReplyToX() {
        return replyToX;
    }

    public int getReplyToY() {
        return replyToY;
    }

    public String getReplyToUser() {
        return replyToUser;
    }

    @Override
    public String summary() {
        return String.format("User not found. Telling (%d, %d, %s)", replyToX, replyToY, replyToUser);
    }
}
