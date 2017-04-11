package ids_tp1_6;

import ids.tp1.phone.Opcode;
import ids.tp1.phone.Person;
import ids.tp1.phone.RegistryClient;

import java.io.*;
import java.net.Socket;

public class PtPClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: ...");
            System.exit(0);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        System.out.println("Connecting to " + hostName + " port " + portNumber);

        try (
                Socket socket = new Socket(hostName, portNumber);
                ObjectOutputStream out =  new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            RegistryClient registry = new RegistryClient(in, out);
            System.out.println("Connected !");

            for (;;) {
                Opcode opcode = getOpcode(stdIn);

                switch (opcode) {
                    case ADD:
                        doAdd(stdIn, registry);
                        break;
                    case GET_PHONE:
                        doGetPhone(stdIn, registry);
                        break;
                    case GET_ALL:
                        doGetAll(stdIn, registry);
                        break;
                    case SEARCH:
                        doSearch(stdIn, registry);
                        break;
                }
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static Opcode getOpcode(BufferedReader stdIn) throws IOException {
        for (;;) {
            System.out.println("Choose your operations : ");
            System.out.println(" - add");
            System.out.println(" - getPhone");
            System.out.println(" - getAll");
            System.out.println(" - search");

            String op = stdIn.readLine();

            switch (op) {
                case "add":
                    return Opcode.ADD;
                case "getPhone":
                    return Opcode.GET_PHONE;
                case "getAll":
                    return Opcode.GET_ALL;
                case "search":
                    return Opcode.SEARCH;
                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }

    private static void doAdd(BufferedReader stdIn, RegistryClient registry) throws IOException {
        System.out.println("Enter a name : ");
        String name = stdIn.readLine();
        System.out.println("Enter a number : ");
        String phoneNumber = stdIn.readLine();
        Person p = new Person(name, phoneNumber);
        registry.add(p);
    }

    private static void doGetAll(BufferedReader stdIn, RegistryClient registry) {
        Iterable<Person> all = registry.getAll();
        for (Person person : all) {
            System.out.println(" - " + person);
        }
    }

    private static void doGetPhone(BufferedReader stdIn, RegistryClient registry) throws IOException {
        System.out.println("Enter a name : ");
        String name = stdIn.readLine();
        String phoneNumber = registry.getPhone(name);
        System.out.println("Found " + phoneNumber);
    }

    private static void doSearch(BufferedReader stdIn, RegistryClient registry) throws IOException {
        System.out.println("Enter a name : ");
        String name = stdIn.readLine();
        Person person = registry.search(name);
        System.out.println("Found " + person);
    }
}
