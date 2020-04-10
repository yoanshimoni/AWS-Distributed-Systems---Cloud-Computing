package Local;

import java.util.UUID;


public class localApp {

    public static void main(String[] args) {

        // Obtain a unique id for this local app
        final String localAppId = UUID.randomUUID().toString();

        final String L2M_QUEUE = "L2M_Queue"; // local to manager

        Ec2 ec2 = new Ec2(); //create ec2 manager if not already exists
        S3BucketOps s3 = new S3BucketOps(args[0], localAppId, args[1]); //create a bucket and use the local appid for the name
        int numOfWorkers = Integer.parseInt(args[2]);
        sqsOPS sqsOPS = new sqsOPS();
        String L2M_QUEUE_URL = sqsOPS.createSQS(L2M_QUEUE);
        Task task = new Task(s3.bucketName, numOfWorkers, args[1] + localAppId, localAppId);
        sqsOPS.SendMessage(task.toString(), L2M_QUEUE_URL); // sent a message with the Task to L2M_Queue


    }

}
