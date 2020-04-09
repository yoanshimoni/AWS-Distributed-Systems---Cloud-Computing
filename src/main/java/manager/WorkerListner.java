package manager;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class WorkerListner implements MessageListener {

    private ObjectMapper mapper;
    String QUEUE_URL;

    public WorkerListner(String QUEUE_URL) {
        this.mapper = new ObjectMapper();
        this.QUEUE_URL = QUEUE_URL;
    }

    @Override
    public void onMessage(Message message) {



        NewPDFtask newPDFtask = parseMsg(message);
        System.out.println(newPDFtask.toString());

        try {
            message.acknowledge();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while acknowledging message");
        }
    }

    private NewPDFtask parseMsg(Message msg) {
        NewPDFtask pdfTask = null;
        try {
            String message = ((TextMessage) msg).getText();
            pdfTask = mapper.readValue(message, NewPDFtask.class);
        } catch (IOException | JMSException ex) {
            System.err.println("Caught an exception while parsing message");
            ex.printStackTrace();
            System.exit(1);
        }
        return pdfTask;
    }
}
