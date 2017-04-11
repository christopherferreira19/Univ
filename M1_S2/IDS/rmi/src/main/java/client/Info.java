package client;

import shared.Info_itf;

import java.rmi.RemoteException;

public class Info implements Info_itf{

    private final String name;

    public Info(String name) {
        this.name = name;
    }
    @Override
    public String getName() throws RemoteException {
        return name;
    }
}
