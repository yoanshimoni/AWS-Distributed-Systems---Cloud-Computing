package Local;


import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import software.amazon.awssdk.regions.Region;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class QueueConnection {
    private SQSConnection connection;


    public QueueConnection(String queueName, MessageListener listener) {

        // Create a new connection factory
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(Region.US_EAST_1.toString())
                        .build());
        try {
            // Create the connection
            connection = connectionFactory.createConnection();

            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Create a consumer for the ManagerToLocalApp queue
            MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));

            // Instantiate and set the message listener for the consumer
            consumer.setMessageListener(listener);
        } catch (JMSException ex) {
            System.err.println("Caught an exception while establishing connection to SQS queue");
            ex.printStackTrace();
            System.exit(1);
        }


    }

    public void start() {
        // Start receiving incoming messages
        try {
            connection.start();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while starting connection to SQS queue");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void close() {
        // Close the connection (and the session)
        try {
            connection.close();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while closing connection to SQS queue");
            ex.printStackTrace();
            System.exit(1);
        }
    }



}