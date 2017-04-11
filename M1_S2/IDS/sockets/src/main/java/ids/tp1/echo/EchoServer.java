package ids.tp1.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

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
                PrintWriter out =  new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("Received a connection !");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received : " + inputLine);
                out.println(inputLine);
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
