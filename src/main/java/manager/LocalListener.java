package manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        download(newTask);

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

    private void download(Task newTask) {
        S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;


        String key = "resources";
        String bucketName = newTask.getBucketName();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client
                .builder()
                .region(region)
                .build();
        ResponseInputStream<GetObjectResponse> s3Object = s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
                ResponseTransformer.toInputStream());
        int numOfFiles = 0;


        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object));
        String line = null;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                System.err.println("Caught an exception while parsing resources file from S3");
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println(line);
            numOfFiles++;
            System.out.println();

        }
        System.out.printf("num of file is %d\n", numOfFiles);

    }
}