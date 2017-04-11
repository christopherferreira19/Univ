package server;

import common.Channel;
import common.Client;
import common.Message;
import common.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelImpl implements Channel {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Usage: java ChannelImpl <hostName> <portNb> <channel1> <channel2> ...");
                return;
            }

            String hostName = args[0];
            Integer portNb = Integer.parseInt(args[1]);
            Registry registry = LocateRegistry.getRegistry(hostName, portNb);

            for (int i = 1; i < args.length; i++) {
                String arg = args[i];
                ChannelImpl chan = new ChannelImpl();
                // registry.bind(arg, (Channel)UnicastRemoteObject.exportObject(chan, 0));
                Channel chan_stub = (Channel) UnicastRemoteObject.exportObject(chan, 0);
                registry.bind(arg, chan_stub);
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private final Map<Client, User> userMap = new HashMap<>();
    private final List<Message> messageList = new ArrayList<>();

    @Override
    public void register(Client client, String name) throws RemoteException {
        User newUser = new User(name);
        if(userMap.containsKey(client)) {
            throw new RuntimeException("client already registered");
        }
        userMap.put(client, newUser);
        broadcast(c -> c.onUserJoined(newUser));
    }

    @Override
    public void unregister(Client client) throws RemoteException {
        User user = userMap.get(client);
        if(user == null) {
            throw new RuntimeException("client not registered");
        }
        userMap.remove(client);
        broadcast(c -> c.onUserLeave(user));
    }

    @Override
    public List<User> getUsers() throws RemoteException {
        List<User> list = new ArrayList<>();
        list.addAll(userMap.values());
        return list;
    }

    @Override
    public List<Message> getMessages() throws RemoteException {
        return messageList;
    }

    @Override
    public boolean post(Client client, String msg) throws RemoteException {
        User currentUser = userMap.get(client);
        if(currentUser == null) {
            return false;
        }

        Message message = new Message(msg, currentUser);
        broadcast(c -> c.onNewMessage(message));
        messageList.add(message);

        return true;
    }

    @Override
    public boolean postTo(Client clientFrom, String clientToStr, String msg) throws RemoteException {
        User userFrom = userMap.get(clientFrom);

        if(userFrom == null)
            return false;

        Client clientTo = null;
        User userTo = null;

        for (Map.Entry<Client, User> entry : userMap.entrySet()) {
            if(entry.getValue().getName().equals(clientToStr))
            {
                clientTo = entry.getKey();
                userTo = entry.getValue();
            }
        }

        if(clientTo == null) {
            return false;
        }

        Message message = new Message(msg, userFrom, userTo);
        clientTo.onNewMessage(message);
        clientFrom.onNewMessage(message);
        return true;
    }

    private void broadcast(BroadcastFunction consumer) {
        for (Client c : userMap.keySet()) {
            try {
                consumer.accept(c);
            }
            catch (RemoteException e) {
                System.out.println("Could not broadcast to " + c);
                e.printStackTrace();
            }
        }
    }

    private interface BroadcastFunction {
        void accept(Client client) throws RemoteException;
    }
}
