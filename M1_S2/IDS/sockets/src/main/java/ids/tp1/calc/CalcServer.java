package ids.tp1.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcServer {

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
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        ) {
            System.out.println("Received a connection !");
            for (;;)  {
                Operator operator = Operator.values()[in.readByte()];
                int left = in.readInt();
                int right = in.readInt();
                int result = op(operator, left, right);
                out.writeInt(result);
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static int op(Operator operator, int left, int right) {
        switch (operator) {
            case PLUS: return left + right;
            case MINUS: return left - right;
            case DIVIDE: return left / right;
            case MULTIPLY: return left * right;
        }

        throw new RuntimeException();
    }
}
