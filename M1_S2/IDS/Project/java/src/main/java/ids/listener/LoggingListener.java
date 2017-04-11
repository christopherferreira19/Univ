package ids.listener;

import ids.entity.EntityId;
import ids.msg.Message;

import java.util.UUID;

public class LoggingListener implements Listener {

    public void header() {
        System.out.println("===========================================================================================================");
        System.out.println("|        From        |         To         |   Summary");
        System.out.println("+--------------------+--------------------+----------------------------------------------------------------");
    }

    @Override
    public void onMessageSending(EntityId fromId, EntityId toId, UUID uuid, Message message) {
    }

    @Override
    public void onMessageReceived(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        System.out.println(String.format("| %-18s | %-18s | %s", fromId, toId, message.summary()));
    }

    @Override
    public void onMessageHandled(EntityId fromId, EntityId toId, UUID uuid, Message message) {
    }
}
