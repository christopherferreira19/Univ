package ids.entity;

import com.rabbitmq.client.Connection;
import ids.msg.*;

import java.util.HashMap;
import java.util.Map;

import static org.msgpack.core.Preconditions.checkState;

public class Registry extends Entity {

    private final int x;
    private final int y;
    private final int level;
    private final int levelsCount;

    Map<String, int[]> map = new HashMap<>();

    public Registry(int x, int y, int level, int levelsCount, Connection connection) {
        super(connection, EntityId.registry(x, y, level));
        this.x = x;
        this.y = y;
        this.level = level;
        this.levelsCount = levelsCount;
    }

    private boolean hasParentRegistry() {
        return level > 0;
    }

    private EntityId parentRegistry() {
        checkState(hasParentRegistry(), "Calling parentRegistry on a registry without a parent");

        int parentX = x / 2;
        int parentY = y / 2;
        return EntityId.registry(parentX, parentY, level - 1);
    }

    private boolean isLastRegistryLevel() {
        return level == levelsCount - 1;
    }

    private EntityId subLevel(int towardsX, int towardsY) {
        if (isLastRegistryLevel()) {
            return EntityId.region(towardsX, towardsY);
        }

        int subLevel = level + 1;
        int subX = towardsX >> (levelsCount - subLevel);
        int subY = towardsY >> (levelsCount - subLevel);
        return EntityId.registry(subX, subY, subLevel);
    }

    private void register(UserRegister register) {
        String username = register.getUsername();
        int[] position= { register.getX(), register.getY() };
        map.put(username, position);
    }

    @Override
    void on(EntityId sender, UserRegister userRegister) {
        register(userRegister);
        if (hasParentRegistry()) {
            send(parentRegistry(), userRegister);
        }
    }

    private void unregister(UserUnregister unregister) {
        String username = unregister.getUsername();
        int[] position = { unregister.getX(), unregister.getY() };
        map.remove(username, position);
    }

    @Override
    void on(EntityId sender, UserUnregister unregister) {
        unregister(unregister);
        if (hasParentRegistry()) {
            send(parentRegistry(), unregister);
        }
    }

    @Override
    void on(EntityId sender, UserLookup userLookup) {
        int[] pos = map.get(userLookup.getQuery());
        if (pos != null) {
            EntityId receiverName = subLevel(userLookup.getReplyToX(), userLookup.getReplyToY());
            send(receiverName, new UserFound(
                    userLookup.getReplyToX(),
                    userLookup.getReplyToY(),
                    userLookup.getReplyToUser(),
                    userLookup.getQuery(),
                    pos[0],
                    pos[1]));
        }
        else if (hasParentRegistry()) {
            send(parentRegistry(), userLookup);
        }
        else {
            EntityId receiverName = subLevel(userLookup.getReplyToX(), userLookup.getReplyToY());
            send(receiverName, new UserNotFound(
                    userLookup.getReplyToX(),
                    userLookup.getReplyToY(),
                    userLookup.getReplyToUser()));
        }
    }

    @Override
    void on(EntityId sender, UserFound userFound) {
        EntityId receiverName = subLevel(userFound.getReplyToX(), userFound.getReplyToY());
        send(receiverName, userFound);
    }

    @Override
    void on(EntityId sender, UserNotFound userNotFound) {
        EntityId receiverName = subLevel(userNotFound.getReplyToX(), userNotFound.getReplyToY());
        send(receiverName, userNotFound);
    }
}
