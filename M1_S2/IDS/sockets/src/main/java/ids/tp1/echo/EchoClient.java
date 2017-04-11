package ids.tp1.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {

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
                PrintWriter out =  new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            System.out.println("Connected !");
            String userInput;


                out.println(userInput);
                String echo = in.readLine();
                if (echo == null) {
                    System.out.println("Received null");
                    break;
                }
                System.out.println("echo : " + echo);
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
