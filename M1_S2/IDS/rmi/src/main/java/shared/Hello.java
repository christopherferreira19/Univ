package shared;

import client.Info;

import java.rmi.*;

public interface Hello extends Remote {
	String sayHello()  throws RemoteException;
	String sayHello(String name)  throws RemoteException;
	String sayHello(Info_itf info) throws RemoteException;
}
