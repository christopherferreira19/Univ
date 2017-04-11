package server;

import shared.*;

import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.rmi.registry.*;

public class HelloServer {

    public static void  main(String [] args) {
        try {
            // Create a Hello remote object
            RegistryImpl reg = new RegistryImpl();
            HelloImpl h = new HelloImpl ("Hello world !");
            Hello2 hello2= new Hello2impl("Hello2", reg);
            Hello h_stub = (Hello) UnicastRemoteObject.exportObject(h, 0);
            Hello2 h2_stub = (Hello2) UnicastRemoteObject.exportObject(hello2, 0);
            shared.Registry reg_stub = (shared.Registry) UnicastRemoteObject.exportObject(reg, 0);

            // Register the remote object in RMI registry with a given identifier
            Registry registry= LocateRegistry.getRegistry();
            registry.bind("HelloService", h_stub);
            registry.bind("Hello2Service", h2_stub);
            registry.bind("RegistryService", reg_stub);

            System.out.println ("Server ready");
        } catch (Exception e) {
            System.err.println("Error on server :" + e) ;
            e.printStackTrace();
        }
    }
}
