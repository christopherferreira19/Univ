package ids.entity;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import ids.listener.Listener;
import ids.msg.*;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Entity {

    private static MessagePack.PackerConfig PACKER_CONFIG = new MessagePack.PackerConfig();

    private final EntityId entityId;
    private final Channel channel;
    final List<Listener> listeners;

    Entity(Connection connection, EntityId entityId) {
        this.entityId = entityId;
        try {
            this.channel = connection.createChannel();
            channel.queueDeclare(entityId.toString(), true, false, false, null);
            channel.basicConsume(entityId.toString(), true, new EntityConsumer(channel, this));
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }

        this.listeners = new ArrayList<>();
    }

    public void send(EntityId receiver, Message message) {
        boolean forUser = receiver.getType() == EntityId.Type.USER;
        UUID uuid = UUID.randomUUID();

        for (Listener listener : listeners) {
            listener.onMessageSending(getId(), receiver, uuid, message);
        }
        if (forUser) {
            // Users are not part of the system so we assume
            // they will receive and handle the message quickly enough
            for (Listener listener : listeners) {
                listener.onMessageReceived(getId(), receiver, uuid, message);
            }
            for (Listener listener : listeners) {
                listener.onMessageHandled(getId(), receiver, uuid, message);
            }
        }

        try {
            MessageBufferPacker packer = PACKER_CONFIG.newBufferPacker();
            entityId.encode(packer);
            encodeUUID(receiver, uuid, packer);
            message.encode(packer);
            channel.basicPublish("", receiver.toString(), null, packer.toByteArray());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encodeUUID(EntityId receiver, UUID uuid, MessageBufferPacker packer) throws IOException {
        if (receiver.getType() != EntityId.Type.USER) {
            packer.packLong(uuid.getLeastSignificantBits());
            packer.packLong(uuid.getMostSignificantBits());
        }
    }

    public final EntityId getId() {
        return entityId;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void unexpectedMessage(Message msg) {
        throw new RuntimeException(
                "Unexpected message " + msg.getClass().getSimpleName()
                + " received by " + this.getClass().getSimpleName());
    }

    void on(EntityId sender, UserConnect userConnect) {
        unexpectedMessage(userConnect);
    }

    void on(EntityId sender, UserDisconnect userDisconnect) {
        unexpectedMessage(userDisconnect);
    }

    void on(EntityId sender, UserRegister userRegister) {
        unexpectedMessage(userRegister);
    }

    void on(EntityId sender, UserUnregister userUnregister) {
        unexpectedMessage(userUnregister);
    }

    void on(EntityId sender, UserLookup userLookup) {
        unexpectedMessage(userLookup);
    }

    void on(EntityId sender, UserFound userFound) {
        unexpectedMessage(userFound);
    }

    void on(EntityId sender, UserNotFound userNotFound) {
        unexpectedMessage(userNotFound);
    }

    void on(EntityId sender, CallRequest callRequest) {
        unexpectedMessage(callRequest);
    }

    void on(EntityId sender, CallAcceptOk callAcceptOk) {
        unexpectedMessage(callAcceptOk);
    }

    void on(EntityId sender, CallAcceptKo callAcceptKo) {
        unexpectedMessage(callAcceptKo);
    }

    void on(EntityId sender, CallAccept callAccept) {
        unexpectedMessage(callAccept);
    }

    void on(EntityId sender, CallEnd callEnd) {
        unexpectedMessage(callEnd);
    }

    void on(EntityId sender, CallUnavailable callUnavailable) {
        unexpectedMessage(callUnavailable);
    }

    void on(EntityId sender, CallEstablished callEstablished) {
        unexpectedMessage(callEstablished);
    }

    void on(EntityId sender, CallSend callSend) {
        unexpectedMessage(callSend);
    }

    void on(EntityId sender, CallReceive callReceive) {
        unexpectedMessage(callReceive);
    }

    void on(EntityId sender, Forward forward) {
        unexpectedMessage(forward);
    }

    void on(EntityId sender, Move move) {
        unexpectedMessage(move);
    }

    void on(EntityId sender, Moving moving) {
        unexpectedMessage(moving);
    }

    void on(EntityId sender, Moved moved) {
        unexpectedMessage(moved);
    }
}
