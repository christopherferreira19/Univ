package server;

import shared.Accounting;
import shared.Hello2;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Hello2impl implements Hello2 {

    private final String msg;
    private final RegistryImpl reg;

    public Hello2impl(String msg, RegistryImpl reg) {
        this.msg = msg;
        this.reg = reg;
    }

    @Override
    public String sayHello(Accounting client) throws RemoteException {
        if (!reg.clientMap.containsKey(client)) {
            throw new RuntimeException("client not registered");
        }

        int val = reg.clientMap.compute(client, (c, i) -> i + 1);

        if(val % 10 == 0) {
            client.numberOfCall(val);
        }

        return msg;
    }
}
