package Workers;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class WorkerListenerToManager implements MessageListener {
    private ObjectMapper objectMapper;

    public WorkerListenerToManager() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message msg) {

        System.out.println(parseMsg(msg).getURL());


        // Create a task to download and parse S3 file content

        // Tell SQS to delete the message

        try {
            msg.acknowledge();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while acknowledging message");
        }
    }

    private NewPDFtask parseMsg(Message msg) {
        NewPDFtask task = null;
        try {
            String message = ((TextMessage) msg).getText();
            task = objectMapper.readValue(message, NewPDFtask.class);
        } catch (IOException | JMSException ex) {
            System.err.println("Caught an exception while parsing message");
            ex.printStackTrace();
            System.exit(1);
        }
        return task;
    }

}