package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello2 extends Remote{
    String sayHello(Accounting client) throws RemoteException;
}
