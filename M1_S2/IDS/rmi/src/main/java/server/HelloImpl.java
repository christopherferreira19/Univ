package server;

import client.Info;
import shared.Hello;
import shared.Info_itf;

import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
 
	public HelloImpl(String s) {
		message = s ;
	}

	public String sayHello() throws RemoteException {
		return message ;
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return message + " From:" + name;
	}

	@Override
	public String sayHello(Info_itf info) throws RemoteException {
		return message + " From:" + info.getName();
	}
}

