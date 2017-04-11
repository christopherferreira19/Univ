package ids.entity;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import ids.listener.Listener;
import ids.msg.*;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class EntityConsumer extends DefaultConsumer {

    MessagePack.UnpackerConfig UNPACKER_CONFIG = new MessagePack.UnpackerConfig();

    private final Entity entity;

    public EntityConsumer(Channel channel, Entity entity) {
        super(channel);
        this.entity = entity;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        try {
            doHandleDelivery(body);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void doHandleDelivery(byte[] body) throws IOException {
        MessageUnpacker unpacker = UNPACKER_CONFIG.newUnpacker(body);
        EntityId sender = EntityId.decode(unpacker);
        UUID uuid = decodeUUID(unpacker, sender);

        Message message = Message.decode(unpacker);
        for (Listener e : entity.listeners) {
            e.onMessageReceived(sender, entity.getId(), uuid, message);
        }

        switch (message.getCode()) {
            case USER_CONNECT:
                entity.on(sender, (UserConnect) message);
                break;
            case USER_DISCONNECT:
                entity.on(sender, (UserDisconnect) message);
                break;
            case USER_REGISTER:
                entity.on(sender, (UserRegister) message);
                break;
            case USER_UNREGISTER:
                entity.on(sender, (UserUnregister) message);
                break;
            case USER_LOOKUP:
                entity.on(sender, (UserLookup) message);
                break;
            case USER_FOUND:
                entity.on(sender, (UserFound) message);
                break;
            case USER_NOT_FOUND:
                entity.on(sender, (UserNotFound) message);
                break;

            case CALL_REQUEST:
                entity.on(sender, (CallRequest) message);
                break;
            case CALL_ACCEPT_OK:
                entity.on(sender, (CallAcceptOk) message);
                break;
            case CALL_ACCEPT_KO:
                entity.on(sender, (CallAcceptKo) message);
                break;
            case CALL_ACCEPT:
                entity.on(sender, (CallAccept) message);
                break;
            case CALL_UNAVAILABLE:
                entity.on(sender, (CallUnavailable) message);
                break;
            case CALL_ESTABLISHED:
                entity.on(sender, (CallEstablished) message);
                break;
            case CALL_SEND:
                entity.on(sender, (CallSend) message);
                break;
            case CALL_RECEIVE:
                entity.on(sender, (CallReceive) message);
                break;
            case CALL_END:
                entity.on(sender, (CallEnd) message);
                break;

            case FORWARD:
                entity.on(sender, (Forward) message);
                break;

            case MOVE:
                entity.on(sender, (Move) message);
                break;
            case MOVING:
                entity.on(sender, (Moving) message);
                break;
            case MOVED:
                entity.on(sender, (Moved) message);
                break;

            default:
                throw new RuntimeException("Unknown Message Code : " + message.getCode());
        }


        for (Listener e : entity.listeners) {
            e.onMessageHandled(sender, entity.getId(), uuid, message);
        }
    }

    private UUID decodeUUID(MessageUnpacker unpacker, EntityId sender) throws IOException {
        UUID uuid;
        if (sender.getType() == EntityId.Type.USER) {
            uuid = UUID.randomUUID();
        }
        else {
            long lsb = unpacker.unpackLong();
            long msb = unpacker.unpackLong();
            uuid = new UUID(msb, lsb);
        }
        return uuid;
    }
}
