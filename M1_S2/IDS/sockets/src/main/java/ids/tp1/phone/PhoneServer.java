package ids.tp1.phone;

import ids.tp1.calc.Operator;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.Registry;

public class PhoneServer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: ...");
            System.exit(0);
        }

        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Waiting on port " + portNumber);

        try (
                ServerSocket socket = new ServerSocket(portNumber);
                Socket clientSocket = socket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            Registry_itf registry = new RegistryServer();
            System.out.println("Received a connection !");
            for (;;)  {
                Opcode opcode = Opcode.values()[in.readByte()];
                switch (opcode) {
                    case ADD:
                        doAdd(out, in, registry);
                        break;
                    case GET_PHONE:
                        doGetPhone(out, in, registry);
                        break;
                    case GET_ALL:
                        doGetAll(out, in, registry);
                        break;
                    case SEARCH:
                        doSearch(out, in, registry);
                        break;
                }
            }
        }
        catch (IOException | ClassNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    private static void doGetPhone(ObjectOutputStream out, ObjectInputStream in, Registry_itf registry) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        out.writeObject(registry.getPhone(name));
    }

    private static void doAdd(ObjectOutputStream out, ObjectInputStream in, Registry_itf registry) throws IOException, ClassNotFoundException {
        Person p = (Person) in.readObject();
        registry.add(p);
    }

    private static void doGetAll(ObjectOutputStream out, ObjectInputStream in, Registry_itf registry) throws IOException {
        out.writeObject(registry.getAll());
    }

    private static void doSearch(ObjectOutputStream out, ObjectInputStream in, Registry_itf registry) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        out.writeObject(registry.search(name));
    }
}
