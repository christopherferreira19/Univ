package ids.entity;

import com.rabbitmq.client.Connection;
import ids.msg.*;

import java.util.HashMap;
import java.util.Map;

import java.util.Random;

import static org.msgpack.core.Preconditions.checkState;

public class Region extends Entity {

    private static final Random rand = new Random();

    private final int x;
    private final int y;
    private final int level;

    private final Map<String, UserState> users;

    public Region(int x, int y, int level, Connection connection) {
        super(connection, EntityId.region(x, y));
        this.x = x;
        this.y = y;
        this.level = level;
        this.users = new HashMap<>();
    }

    private EntityId parentRegistry() {
        int parentX = x / 2;
        int parentY = y / 2;
        return EntityId.registry(parentX, parentY, level - 1);
    }

    @Override
    void on(EntityId sender, UserConnect userConnect) {
        users.put(userConnect.getUsername(), UserState.registered());
        UserRegister msg = new UserRegister(userConnect.getUsername(), x, y);
        this.send(parentRegistry(), msg);
    }

    private String describe(UserState state) {
        return state == null
                ? "Unknown"
                : state.describe();
    }

    @Override
    void on(EntityId sender, CallRequest callRequest) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.Registered)) {
            System.out.println(String.format("Discarding CallRequest from user @%s[%s]",
                    user, describe(userState)));
            return;
        }

        users.put(user, UserState.awaitingLookup(callRequest.getCallee()));
        send(parentRegistry(), new UserLookup(
                x, y, user, callRequest.getCallee()));
    }

    @Override
    void on(EntityId sender, UserFound userFound) {
        String user = userFound.getReplyToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.AwaitingLookup)) {
            System.out.println(String.format("Discarding UserFound for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.AwaitingLookup lookup = (UserState.AwaitingLookup) userState;
        users.put(user, lookup.found(userFound.getFoundX(), userFound.getFoundY()));
        CallAccept callAccept = new CallAccept(user);
        startForwarding(user, userFound.getFoundX(), userFound.getFoundY(), userFound.getQuery(), callAccept);
    }

    @Override
    void on(EntityId sender, UserNotFound userNotFound) {
        String user = userNotFound.getReplyToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.AwaitingLookup)) {
            System.out.println(String.format("Discarding UserNotFound for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        users.put(user, UserState.registered());
        send(EntityId.user(user), new CallUnavailable());
    }

    private void startForwarding(String fromUser, int toX, int toY, String toUser, Message msg) {
        Forward forward = new Forward(x, y, fromUser, toX, toY, toUser, msg);
        onForward(forward);
    }

    private void startForwarding(String fromUser, UserState.OngoingCall call, Message msg) {
        Forward forward = new Forward(x, y, fromUser, call.getCalleeX(), call.getCalleeY(), call.getUsername(), msg);
        onForward(forward);
    }

    @Override
    void on(EntityId sender, Forward forward) {
        onForward(forward);
    }

    private void onForward(Forward forward) {
        if (x == forward.getToX() && y == forward.getToY()) {
            Message message = forward.getMsg();
            switch (message.getCode()) {
                case MOVING:
                    onForwarded(forward, (Moving) message);
                    return;
                case MOVED:
                    onForwarded(forward, (Moved) message);
                    return;
            }

            String toUser = forward.getToUser();
            UserState userState = users.get(toUser);
            if (userState instanceof UserState.Moving) {
                ((UserState.Moving) userState).getQueue().add(forward);
                return;
            }
            else if (userState instanceof UserState.Moved) {
                UserState.Moved moved = (UserState.Moved) userState;
                Forward newForward = new Forward(
                        forward.getFromX(), forward.getFromY(), forward.getFromUser(),
                        moved.getToX(), moved.getToY(), forward.getToUser(),
                        message);
                onForward(newForward);
                return;
            }

            switch (message.getCode()) {
                case CALL_ACCEPT:
                    onForwarded(forward, (CallAccept) message);
                    return;
                case CALL_ESTABLISHED:
                    onForwarded(forward, (CallEstablished) message);
                    return;
                case CALL_UNAVAILABLE:
                    onForwarded(forward, (CallUnavailable) message);
                    return;
                case CALL_RECEIVE:
                    onForwarded(forward, (CallReceive) message);
                    return;
                case CALL_END:
                    onForwarded(forward, (CallEnd) message);
                    return;
            }
        }
        else {
            int dx = x - forward.getToX();
            int dy = y - forward.getToY();

            int nextX, nextY;
            if(dx == 0) {
                nextY = y - Integer.signum(dy);
                nextX = x;
            }
            else if (dy == 0) {
                nextX = x - Integer.signum(dx);
                nextY = y;
            }
            else if (rand.nextBoolean()) {
                nextY = y - Integer.signum(dy);
                nextX = x;
            }
            else {
                nextX = x - Integer.signum(dx);
                nextY = y;
            }

            send(EntityId.region(nextX, nextY), forward);
        }
    }

    private void onForwarded(Forward forward, CallAccept callAccept) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (userState == null) {
            // TODO: Maybe due to moving
            System.out.println("TODO !");
        }
        else if (userState instanceof UserState.Registered) {
            String userFrom = forward.getFromUser();
            users.put(user, UserState.establishingCall(userFrom, forward.getFromX(), forward.getFromY()));
            send(EntityId.user(user), new CallAccept(userFrom));
        }
        else {
            String userFrom = forward.getFromUser();
            users.put(user, UserState.establishingCall(userFrom, forward.getFromX(), forward.getFromY()));
            startForwarding(user, forward.getFromX(), forward.getFromY(), forward.getFromUser(), new CallUnavailable());
        }

    }

    @Override
    void on(EntityId sender, CallAcceptOk callAcceptOk) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.EstablishingCall)) {
            System.out.println(String.format("Discarding CallAcceptOk for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall ongoingCall = ((UserState.EstablishingCall) userState).established();
        users.put(user, ongoingCall);
        CallEstablished established = new CallEstablished();
        startForwarding(user, ongoingCall, established);
    }

    @Override
    void on(EntityId sender, CallAcceptKo callAcceptKo) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.EstablishingCall)) {
            System.out.println(String.format("Discarding CallAcceptOk for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.EstablishingCall establishingCall = (UserState.EstablishingCall) userState;
        users.put(user, UserState.registered());
        CallUnavailable callUnavailable = new CallUnavailable();
        startForwarding(user, establishingCall.getCalleeX(), establishingCall.getCalleeY(),
                establishingCall.getUsername(), callUnavailable);
    }

    private void onForwarded(Forward forward, CallEstablished callEstablished) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.EstablishingCall)) {
            System.out.println(String.format("Discarding CallEstablished for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall onGoingCall = ((UserState.EstablishingCall) userState).established();
        users.put(user, onGoingCall);

        send(EntityId.user(user), callEstablished);
    }

    private void onForwarded(Forward forward, CallUnavailable callUnavailable) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.EstablishingCall)) {
            System.out.println(String.format("Discarding CallUnavailable for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        users.put(user, UserState.registered());
        send(EntityId.user(user), callUnavailable);
    }

    @Override
    void on(EntityId sender, CallSend callSend) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.OngoingCall)) {
            System.out.println(String.format("Discarding CallSend for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall userOngoingCall = (UserState.OngoingCall) userState;
        CallReceive callReceive = new CallReceive(user, callSend.getContent());
        startForwarding(user, userOngoingCall, callReceive);
    }

    private void onForwarded(Forward forward, CallReceive callReceive) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.OngoingCall)) {
            System.out.println(String.format("Discarding CallSend for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall ongoingCall = (UserState.OngoingCall) userState;
        ongoingCall.update(forward.getFromX(), forward.getFromY());
        send(EntityId.user(user), callReceive);
    }

    @Override
    void on(EntityId sender, CallEnd callEnd) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.OngoingCall)) {
            System.out.println(String.format("Discarding CallEnd for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall onGoingCall = (UserState.OngoingCall) userState;
        UserState.Registered newUserState = onGoingCall.disconnect();
        users.put(user, newUserState);

        startForwarding(user, onGoingCall, callEnd);
    }

    private void onForwarded(Forward forward, CallEnd callEnd) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.OngoingCall)) {
            System.out.println(String.format("Discarding CallEnd for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.OngoingCall state = (UserState.OngoingCall) userState;
        UserState.Registered newUserState = state.disconnect();
        users.put(user, newUserState);
        send(EntityId.user(user), callEnd);
    }

    @Override
    void on(EntityId sender, UserDisconnect userDisconnect) {
        String user = ((EntityId.User) sender).getUsername();
        UserState userState = users.get(user);

        if (userState instanceof UserState.OngoingCall) {
            UserState.OngoingCall ongoinCall = (UserState.OngoingCall) userState;
            startForwarding(user, ongoinCall, new CallEnd());
        }

        UserUnregister msg = new UserUnregister(user, x, y);
        this.send(parentRegistry(), msg);
        users.remove(user);
    }

    @Override
    void on(EntityId sender, Move move) {
        String user = ((EntityId.User) sender).getUsername();
        UserState oldState = users.get(user);
        users.put(user, UserState.moving(move.getX(), move.getY()));
        startForwarding(user, move.getX(), move.getY(), user, new Moving(oldState));
    }

    private void onForwarded(Forward forward, Moving moving) {
        String user = forward.getFromUser();
        users.put(user, moving.getState());
        send(parentRegistry(), new UserRegister(user, x, y));
        send(EntityId.user(user), new MoveOk(x, y));
        startForwarding(user, forward.getFromX(), forward.getFromY(), user, new Moved());
    }


    private void onForwarded(Forward forward, Moved moved) {
        String user = forward.getToUser();
        UserState userState = users.get(user);
        if (!(userState instanceof UserState.Moving)) {
            System.out.println(String.format("Discarding Moved for caller @%s[%s]",
                    user, describe(userState)));
            return;
        }

        UserState.Moving moving = (UserState.Moving) userState;
        users.put(forward.getFromUser(), moving.moved());
        send(parentRegistry(), new UserUnregister(user, x, y));
        for (Forward msg : moving.getQueue()) {
            Forward newForward = new Forward(
                    msg.getFromX(), msg.getFromY(), msg.getFromUser(),
                    moving.getToX(), moving.getToY(), msg.getToUser(),
                    msg.getMsg());
            onForward(newForward);
        }
    }
}
