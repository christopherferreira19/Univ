package client;

import shared.Accounting;
import shared.Hello;
import shared.Hello2;
import shared.Info_itf;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class HelloClient {
    public static void main(String [] args) {

        try {
            if (args.length < 1) {
                System.out.println("Usage: java HelloClient <rmiregistry host>");
                return;
            }

            String host = args[0];
            Accounting acc = new AccountingImpl();
            System.out.println(acc);
            // Get remote object reference
            Registry registry = LocateRegistry.getRegistry(host);
            Hello h = (Hello) registry.lookup("HelloService");
            Hello2 h2 = (Hello2) registry.lookup("Hello2Service");
            shared.Registry reg = (shared.Registry) registry.lookup("RegistryService");
            Info info = new Info("me2");

            Info_itf info_stub = (Info_itf) UnicastRemoteObject.exportObject(info, 0);
            Accounting acc_stub = (Accounting) UnicastRemoteObject.exportObject(acc, 0);

            reg.register(acc_stub);

            // Remote method invocation
            String res = h.sayHello();
            System.out.println(res);
            res = h.sayHello("me1");
            System.out.println(res);
            res = h.sayHello(info_stub);
            System.out.println(res);


            for (int i = 0; i < 40; i++) {
                res = h2.sayHello(acc_stub);
                System.out.println(res);
            }

        } catch (Exception e)  {
            System.err.println("Error on client: " + e);
        }
    }
}