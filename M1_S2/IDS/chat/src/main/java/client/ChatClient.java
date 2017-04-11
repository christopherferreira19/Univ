package client;

import common.Channel;
import common.Client;
import common.Message;
import common.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient implements Client {

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("Usage: java HelloClient <rmiregistry host> <channel> <name> <portNb>");
                return;
            }

            String hostName = args[0];
            String channelName = args[1];
            String userName = args[2];
            Integer portNb = Integer.parseInt(args[3]);

            Registry registry = LocateRegistry.getRegistry(hostName, portNb);
            Channel channel = (Channel) registry.lookup(channelName);
            ChatClient client = new ChatClient(channel);
            client.stub = (Client) UnicastRemoteObject.exportObject(client, 0);

            channel.register(client.stub, userName);
            client.printUsersAndHistory();
            client.loop();
            channel.unregister(client.stub);
            UnicastRemoteObject.unexportObject(client, false);
        }
        catch (NotBoundException exc) {
            System.out.println("Unkown channel name");
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private final Channel channel;
    private Client stub;

    public ChatClient(Channel channel) {
        this.channel = channel;
    }

    private void printUsersAndHistory() throws RemoteException {
        System.out.println("Connected users :");
        for (User user : channel.getUsers()) {
            System.out.println(" - " + user);
        }
        System.out.println("History :");
        for (Message message : channel.getMessages()) {
            System.out.println(message);
        }
    }

    private void loop() throws IOException {
        String input;
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            while ((input = stdIn.readLine()) != null) {
                if(input.isEmpty())
                    continue;

                switch (input.charAt(0)) {
                    case '@':
                        Integer index = input.indexOf(' ');
                        channel.postTo(stub, input.substring(1, index), input.substring(index));
                        break;
                    default:
                        channel.post(stub, input);
                }
            }
        }
    }

    @Override
    public void onUserJoined(User user) throws RemoteException {
        System.out.println(user + " joined");
    }

    @Override
    public void onUserLeave(User user) throws RemoteException {
        System.out.println(user + " left");
    }

    @Override
    public void onNewMessage(Message message) throws RemoteException {
        System.out.println(message);
    }
}
