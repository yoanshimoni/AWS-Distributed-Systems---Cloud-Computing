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
        DonePDFTask donePDFTask = parseMsg(message);
//        Manager.filesPool.execute(new AppendToSummaryFile(donePDFTask));
        System.out.println(donePDFTask.getSummaryTask());

        try {
            message.acknowledge();
        } catch (JMSException ex) {
            System.err.println("Caught an exception while acknowledging message");
        }
    }

    private DonePDFTask parseMsg(Message msg) {
        DonePDFTask donePDFTask = null;
        try {
            String message = ((TextMessage) msg).getText();
            donePDFTask = mapper.readValue(message, DonePDFTask.class);
        } catch (IOException | JMSException ex) {
            System.err.println("Caught an exception while parsing message");
            ex.printStackTrace();
            System.exit(1);
        }
        return donePDFTask;
    }
}
