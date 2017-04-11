package ids.listener;

import ids.entity.EntityId;
import ids.msg.Message;

import java.util.UUID;

public interface Listener {

    void onMessageSending(EntityId fromId, EntityId toId, UUID uuid, Message message);

    void onMessageReceived(EntityId fromId, EntityId toId, UUID uuid, Message message);

    void onMessageHandled(EntityId fromId, EntityId toId, UUID uuid, Message message);
}
