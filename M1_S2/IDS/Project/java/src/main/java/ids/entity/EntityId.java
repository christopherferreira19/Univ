package ids.entity;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.Objects;

public abstract class EntityId {

    public enum Type {
        USER,
        REGION,
        REGISTRY
    }

    public static User user(String username) {
        return new User(username);
    }

    public static Region region(int x, int y) {
        return new Region(x, y);
    }

    public static Registry registry(int x, int y, int level) {
        return new Registry(x, y, level);
    }


    public static EntityId decode(MessageUnpacker unpacker) {
        try {
            Type type = Type.values()[unpacker.unpackInt()];
            switch (type) {
                case USER:
                    String username = unpacker.unpackString();
                    return new User(username);
                case REGION: {
                    int x = unpacker.unpackInt();
                    int y = unpacker.unpackInt();
                    return new Region(x, y);
                }
                case REGISTRY: {
                    int x = unpacker.unpackInt();
                    int y = unpacker.unpackInt();
                    int size = unpacker.unpackInt();
                    return new Registry(x, y, size);
                }
                default:
                    throw new RuntimeException();
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private EntityId() {
    }

    public abstract Type getType();

    public void encode(MessagePacker packer) {
        try {
            packer.packInt(getType().ordinal());
            doEncode(packer);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    protected abstract void doEncode(MessagePacker packer) throws IOException;

    public static class User extends EntityId {

        private final String username;

        private User(String username) {
            this.username = username;
        }

        @Override
        public Type getType() {
            return Type.USER;
        }

        public String getUsername() {
            return username;
        }

        @Override
        protected void doEncode(MessagePacker packer) throws IOException {
            packer.packString(username);
        }

        @Override
        public int hashCode() {
            return username.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof User)) {
                return false;
            }

            User other = (User) obj;
            return username.equals(other.username);
        }

        public String toString() {
            return String.format("User#%s", username);
        }
    }

    public static class Region extends EntityId {

        private final int x;
        private final int y;

        private Region(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Type getType() {
            return Type.REGION;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        protected void doEncode(MessagePacker packer) throws IOException {
            packer.packInt(x);
            packer.packInt(y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Region)) {
                return false;
            }

            Region other = (Region) obj;
            return x == other.x && y == other.y;
        }

        public String toString() {
            return String.format("Region#(%d,%d)", x, y);
        }
    }

    public static class Registry extends EntityId {

        private final int x;
        private final int y;
        private final int level;

        private Registry(int x, int y, int level) {
            this.x = x;
            this.y = y;
            this.level = level;
        }

        @Override
        public Type getType() {
            return Type.REGISTRY;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getLevel() {
            return level;
        }

        @Override
        protected void doEncode(MessagePacker packer) throws IOException {
            packer.packInt(x);
            packer.packInt(y);
            packer.packInt(level);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, level);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Registry)) {
                return false;
            }

            Registry other = (Registry) obj;
            return x == other.x && y == other.y && level == other.level;
        }

        public String toString() {
            return String.format("Registry#(%d,%d;%d)", x, y, level);
        }
    }
}
