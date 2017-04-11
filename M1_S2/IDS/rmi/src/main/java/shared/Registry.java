package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Registry extends Remote {
    void register(Accounting client) throws RemoteException;
}
