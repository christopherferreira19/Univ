package ids.tp1.calc;

import java.io.*;
import java.net.Socket;

public class CalcClient implements Calculator_itf {

    private final DataOutputStream out;
    private final DataInputStream in;

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
                DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            CalcClient client = new CalcClient(out, in);
            System.out.println("Connected !");

            for (;;) {
                Operator operator = getOperator(stdIn);

                int left = getOperande(stdIn);
                int right = getOperande(stdIn);

                int result = client.op(operator, left, right);
                System.out.println("Result is : " + result);
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static Operator getOperator(BufferedReader stdIn) throws IOException {
        for (;;) {
            System.out.println("Choose your operations : ");
            System.out.println(" - Add: enter +");
            System.out.println(" - Subtract: enter -");
            System.out.println(" - Divide: enter /");
            System.out.println(" - Multiply: enter *");

            String op = stdIn.readLine();

            switch (op) {
                case "+":
                    return Operator.PLUS;
                case "-":
                    return Operator.MINUS;
                case "/":
                    return Operator.DIVIDE;
                case "*":
                    return Operator.MULTIPLY;
                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }

    private static int getOperande(BufferedReader stdIn) throws IOException {
        for (;;) {
            try {
                System.out.println("Type a number :");
                return Integer.parseInt(stdIn.readLine());
            }
            catch (NumberFormatException exc) {
                System.out.println("Wrong input");
            }
        }
    }

    CalcClient(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }


    @Override
    public int plus(int left, int right) {
        return op(Operator.PLUS, left, right);
    }

    @Override
    public int minus(int left, int right) {
        return op(Operator.MINUS, left, right);
    }

    @Override
    public int divide(int left, int right) {
        return op(Operator.DIVIDE, left, right);
    }

    @Override
    public int multiply(int left, int right) {
        return op(Operator.MULTIPLY, left, right);
    }

    private int op(Operator operator, int left, int right) {
        try {
            out.writeByte(operator.ordinal());
            out.writeInt(left);
            out.writeInt(right);

            return in.readInt();
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
