import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Ring {
    private final static String QUEUE_NAME = "ring";
    public static final String HEADER_FROM = "From";
    public static final String REQUEST = "Request";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        int MAX_NODE = 3;
        Node[] nodes = new Node[MAX_NODE];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(connection, i , MAX_NODE);
            nodes[i].start();
        }

        Channel chan = connection.createChannel();
        chan.queueDeclare(REQUEST + 1, false, false, false, null);
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_FROM, 1);
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .headers(headers)
                .build();
        chan.basicPublish("", REQUEST + 1, properties, "Hello".getBytes());
    }

    public static class Node extends Thread{

        private Connection connection;
        private Channel chan;
        private int id;
        private int nbNodes;
        private int nextId;

        public Node(Connection connection, int id, int nbNodes) {
            this.connection = connection;
            this.id = id;
            this.nbNodes = nbNodes;
            nextId = (id + 1) % nbNodes;
        }
        @Override public void run () {
            try {
                final Channel chan = connection.createChannel();
                String queueIn = QUEUE_NAME + id;
                final String queueOut = QUEUE_NAME + nextId;
                chan.queueDeclare(queueIn, false, false, false, null);
                chan.queueDeclare(queueOut, false, false, false, null);
                chan.queueDeclare(REQUEST + id, false, false, false, null);

                Consumer consumer = new DefaultConsumer(chan) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(" [" + id + "] Received '" + message + "'");
                        if(!properties.getHeaders().get(HEADER_FROM).equals(nextId)){
                            chan.basicPublish("", queueOut, properties, body);
                            System.out.println(" [" + id + "] Sent '" + message + "'");
                        }
                        else {
                            // chan.close();
                            connection.close();
                        }
                    }
                };
                chan.basicConsume(queueIn, true, consumer);
                chan.basicConsume(REQUEST + id, true, consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
