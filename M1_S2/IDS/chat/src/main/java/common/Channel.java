package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Channel extends Remote {

    void register(Client client, String name) throws RemoteException;

    void unregister(Client client) throws RemoteException;

    List<User> getUsers() throws RemoteException;

    List<Message> getMessages() throws RemoteException;

    boolean post(Client client, String msg) throws RemoteException;

    boolean postTo(Client clientFrom, String clientTo, String msg) throws RemoteException;
}
