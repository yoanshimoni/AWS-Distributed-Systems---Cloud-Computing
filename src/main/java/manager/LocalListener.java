package manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class LocalListener implements MessageListener {
    private int numOfFiles = 0;
    private ObjectMapper mapper;
    private String QUEUE_URL;

    public LocalListener(String QUEUE_URL) {
        this.QUEUE_URL = QUEUE_URL;
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

    public int getNumOfFiles() {
        return numOfFiles;
    }

    private void download(Task newTask) {
        Manager_sqsOPS manager_sqsOPS = new Manager_sqsOPS();
        String key = "resources";
        String bucketName = newTask.getBucketName();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client
                .builder()
                .region(region)
                .build();
        //GetObjectResponse s3Object =
        s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
                ResponseTransformer.toFile(Paths.get("resources.txt")));


        try {
            File myObj = new File("resources.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                this.numOfFiles++;
                String data = myReader.nextLine();
                String[] parts = data.split("\t", 2);
                //TODO send tasks to M2W quque
                Manager.distributionPool.execute(new SendNewPDFtask(this.QUEUE_URL, parts[0], parts[1]));

            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.printf("num of file is %d\n", this.numOfFiles);
    }
}