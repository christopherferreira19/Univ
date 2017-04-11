package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserFound implements Message {

    private final int replyToX;
    private final int replyToY;
    private final String replyToUser;

    private final String query;
    private final int foundX;
    private final int foundY;

    public static UserFound doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserFound(
                unpacker.unpackInt(),
                unpacker.unpackInt(),
                unpacker.unpackString(),
                unpacker.unpackString(),
                unpacker.unpackInt(),
                unpacker.unpackInt());
    }

    public UserFound(int replyToX, int replyToY, String replyToUser,
                     String query, int foundX, int foundY) {
        this.replyToX = replyToX;
        this.replyToY = replyToY;
        this.replyToUser = replyToUser;
        this.query = query;
        this.foundX = foundX;
        this.foundY = foundY;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.USER_FOUND;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packInt(replyToX);
        packer.packInt(replyToY);
        packer.packString(replyToUser);
        packer.packString(query);
        packer.packInt(foundX);
        packer.packInt(foundY);
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

    public String getQuery() {
        return query;
    }

    public int getFoundX() {
        return foundX;
    }

    public int getFoundY() {
        return foundY;
    }

    @Override
    public String summary() {
        return String.format("User @%s is at (%d, %d). Telling (%d, %d, @%s)",
                query, foundX, foundY,
                replyToX, replyToY, replyToUser);
    }
}
