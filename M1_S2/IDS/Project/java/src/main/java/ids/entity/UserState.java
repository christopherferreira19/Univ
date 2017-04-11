package ids.entity;

import ids.msg.Forward;
import ids.msg.Message;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface UserState {

    void encode(MessagePacker packer) throws IOException;

    String describe();

    static Registered registered() {
        return Registered.INSTANCE;
    }

    static AwaitingLookup awaitingLookup(String username) {
        return new AwaitingLookup(username);
    }

    static EstablishingCall establishingCall(String username, int x, int y) { return new EstablishingCall(username, x, y);}

    static Moving moving(int forwardToX, int forwardToY) {
        return new Moving(forwardToX, forwardToY);
    }

    static UserState decode(MessageUnpacker unpacker) throws IOException {
        Type code = Type.values()[unpacker.unpackInt()];
        switch (code) {
            case REGISTERED: return Registered.INSTANCE;
            case AWAITING_LOOKUP: return new AwaitingLookup(unpacker.unpackString());
            case ESTABLISHING_CALL: {
                String username = unpacker.unpackString();
                int x = unpacker.unpackInt();
                int y = unpacker.unpackInt();
                return new EstablishingCall(username, x, y);
            }
            case ONGOING_CALL: {
                String username = unpacker.unpackString();
                int x = unpacker.unpackInt();
                int y = unpacker.unpackInt();
                return new OngoingCall(username, x, y);
            }
            default:
                throw new RuntimeException();
        }
    }

    enum Type {
        REGISTERED,
        AWAITING_LOOKUP,
        ESTABLISHING_CALL,
        ONGOING_CALL
    }

    class Registered implements UserState {

        private static final Registered INSTANCE = new Registered();

        private Registered() {
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            packer.packInt(Type.REGISTERED.ordinal());
        }

        @Override
        public String describe() {
            return "Registered";
        }
    }

    class AwaitingLookup implements UserState {

        private final String username;

        private AwaitingLookup(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            packer.packInt(Type.AWAITING_LOOKUP.ordinal());
            packer.packString(username);
        }

        @Override
        public String describe() {
            return String.format("AwaitingLookup(%s)", username);
        }

        public EstablishingCall found(int calleeX, int calleeY) {
            return new EstablishingCall(username, calleeX, calleeY);
        }
    }

    class EstablishingCall implements UserState {
        private final String username;
        private final int calleeX;
        private final int calleeY;

        private EstablishingCall(String username, int calleeX, int calleeY) {
            this.username = username;
            this.calleeX = calleeX;
            this.calleeY = calleeY;
        }

        public String getUsername() {
            return username;
        }

        public int getCalleeX() {
            return calleeX;
        }

        public int getCalleeY() {
            return calleeY;
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            packer.packInt(Type.ESTABLISHING_CALL.ordinal());
            packer.packString(username);
            packer.packInt(calleeX);
            packer.packInt(calleeY);
        }

        @Override
        public String describe() {
            return String.format("EstablishingCall(%s, %d, %d)", username, calleeX, calleeY);
        }

        public OngoingCall established() {
            return new OngoingCall(username, calleeX, calleeY);
        }
    }

    class OngoingCall implements UserState {

        private final String username;
        private int calleeX;
        private int calleeY;

        private OngoingCall(String username, int calleeX, int calleeY) {
            this.username = username;
            this.calleeX = calleeX;
            this.calleeY = calleeY;
        }

        public String getUsername() {
            return username;
        }

        public int getCalleeX() {
            return calleeX;
        }

        public int getCalleeY() {
            return calleeY;
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            packer.packInt(Type.ONGOING_CALL.ordinal());
            packer.packString(username);
            packer.packInt(calleeX);
            packer.packInt(calleeY);
        }

        @Override
        public String describe() {
            return String.format("OngoingCall (%s %d:%d)", username, calleeX, calleeY);
        }

        public Registered disconnect() {
            return Registered.INSTANCE;
        }

        public void update(int calleeX, int calleeY) {
            this.calleeX = calleeX;
            this.calleeY = calleeY;
        }
    }

    class Moving implements UserState {

        private final int toX;
        private final int toY;
        private final List<Forward> queue;

        private Moving(int toX, int forwardToY) {
            this.toX = toX;
            this.toY = forwardToY;
            this.queue = new ArrayList<>();
        }

        public int getToX() {
            return toX;
        }

        public int getToY() {
            return toY;
        }

        public List<Forward> getQueue() {
            return queue;
        }

        public Moved moved() {
            return new Moved(toX, toY);
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            throw new RuntimeException();
        }

        @Override
        public String describe() {
            return String.format("Moving to (%d, %d)", toX, toY);
        }
    }

    class Moved implements UserState {
        private final int toX;
        private final int toY;

        private Moved(int toX, int forwardToY) {
            this.toX = toX;
            this.toY = forwardToY;
        }

        public int getToX() {
            return toX;
        }

        public int getToY() {
            return toY;
        }

        @Override
        public void encode(MessagePacker packer) throws IOException {
            throw new RuntimeException();
        }

        @Override
        public String describe() {
            return String.format("MovedTo to (%d, %d)", toX, toY);
        }
    }
}


