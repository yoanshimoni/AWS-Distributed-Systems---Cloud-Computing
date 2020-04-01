package manager;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class LocalListener implements MessageListener {

    private ObjectMapper mapper;

    public LocalListener() {
        mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message msg) {

        try {
            System.out.printf("Received: %s\n", ((TextMessage) msg).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // Convert message to NewTask object
        Task newTask = parseMsg(msg);
        System.out.println(newTask.toString());
        // Create a task to download and parse S3 file content
        //Manager.downloadPool.execute(new DownloadAndParse(newTask));

        // Tell SQS to delete the message
        try {
            msg.acknowledge();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while acknowledging message");
        }
    }

    private Task parseMsg(Message msg) {
        Task task = null;
        try {
            String message = ((TextMessage) msg).getText();
            System.out.println("Handling message from local app:\n" + message);
            task = mapper.readValue(message, Task.class);
        } catch (IOException | JMSException ex) {
            System.err.println("Caught an exception while parsing message");
            ex.printStackTrace();
            System.exit(1);
        }
        return task;
    }
}