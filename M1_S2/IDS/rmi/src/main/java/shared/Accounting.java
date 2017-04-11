package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Accounting extends Remote {
    void numberOfCall(int number) throws RemoteException;
}
