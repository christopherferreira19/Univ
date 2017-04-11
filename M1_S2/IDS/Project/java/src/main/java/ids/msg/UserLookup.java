package ids.msg;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class UserLookup implements Message {

    private final int replyToX;
    private final int replyToY;
    private final String replyToUser;

    private final String query;

    public static UserLookup doDecode(MessageUnpacker unpacker) throws IOException {
        return new UserLookup(
                unpacker.unpackInt(),
                unpacker.unpackInt(),
                unpacker.unpackString(),
                unpacker.unpackString());
    }

    public UserLookup(int replyToX, int replyToY, String replyToUser, String query) {
        this.replyToX = replyToX;
        this.replyToY = replyToY;
        this.replyToUser = replyToUser;
        this.query = query;
    }

    @Override
    public MessageCode getCode() {
        return MessageCode.USER_LOOKUP;
    }

    @Override
    public void doEncode(MessagePacker packer) throws IOException {
        packer.packInt(replyToX);
        packer.packInt(replyToY);
        packer.packString(replyToUser);
        packer.packString(query);
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

    @Override
    public String summary() {
        return String.format("Where is @%s ? Tell (%d, %d, @%s)", query,
                replyToX, replyToY, replyToUser);
    }
}
