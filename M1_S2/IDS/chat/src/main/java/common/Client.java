package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

    void onUserJoined(User user) throws RemoteException;

    void onUserLeave(User user) throws RemoteException;

    void onNewMessage(Message message) throws RemoteException;
}
