package server;

import shared.Accounting;
import shared.Registry;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class RegistryImpl implements Registry {
    final Map<Accounting, Integer> clientMap = new HashMap<>();

    @Override
    public void register(Accounting client) throws RemoteException {
        if (clientMap.containsKey(client)) {
            throw new RuntimeException("client already registered");
        }
        clientMap.put(client, 0);
    }
}
