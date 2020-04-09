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
import java.util.Calendar;
import java.util.Scanner;

public class LocalListener implements MessageListener {
    private int numOfFiles = 0;
    private int PDFPerWorker = 0;
    private ObjectMapper mapper;
    private String M2W_QUEUE_URL;

    public LocalListener(String QUEUE_URL) {
        this.M2W_QUEUE_URL = QUEUE_URL; //
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
        this.PDFPerWorker = newTask.getNumOfWorkers();
        download(newTask);
        int numOfWorkers = numOfFiles / newTask.getNumOfWorkers();
        if (numOfWorkers > 3) {
            numOfWorkers = 2;
        }
        Manager.nodesCreationPool.execute(new CreateWorkers(numOfWorkers));


        // Create a task to download and parse S3 file content

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
        String key = newTask.getKey();
        String bucketName = newTask.getBucketName();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client
                .builder()
                .region(region)
                .build();
        //GetObjectResponse s3Object =
        // this is a unique name , trust me i debugged it , a unique name is the best way
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("resources");
        stringBuilder.append(Calendar.getInstance().getTimeInMillis());
        stringBuilder.append(".txt");

        s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
                ResponseTransformer.toFile(Paths.get(stringBuilder.toString())));

        System.out.printf("Downloaded the file, saved to be:\t%s\n", stringBuilder.toString());

        try {
            File myObj = new File(stringBuilder.toString());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                this.numOfFiles++;
                String data = myReader.nextLine();
                String[] parts = data.split("\t", 2);
                Manager.distributionPool.execute(new SendNewPDFtask(this.M2W_QUEUE_URL,
                        parts[0], parts[1], this.PDFPerWorker));

            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.printf("number of files is %d\n", this.numOfFiles);
    }
}