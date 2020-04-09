package Local;

import java.util.UUID;

/*The application resides on a local (non-cloud) machine. Once started, it reads the input file from the user, and:

Checks if a Manager node is active on the EC2 cloud. If it is not, the application will start the Manager node.
Uploads the file to S3.
Sends a message to an SQS queue, stating the location of the file on S3
TODO:
Checks an SQS queue for a message indicating the process is done and the response (the summary file) is available on S3.
Downloads the summary file from S3, and create an html file representing the results.
Sends a termination message to the Manager if it was supplied as one of its input arguments.*/
public class localApp {

    public static void main(String[] args) {

        // Obtain a unique id for this local app
        final String localAppId = UUID.randomUUID().toString();

        final String L2M_QUEUE = "L2M_Queue"; // local to manager
//        final String M2W_QUEUE = "M2W_Queue"+localAppId; // manager to workers

        Ec2 ec2 = new Ec2(); //create ec2 manager if not already exists
        S3BucketOps s3 = new S3BucketOps(args[0], localAppId); //create a bucket and use the local appid for the name
        int numOfWorkers = Integer.parseInt(args[2]);
        sqsOPS sqsOPS = new sqsOPS();
        String L2M_QUEUE_URL = sqsOPS.createSQS(L2M_QUEUE);
//        sqsOPS.createSQS(M2W_QUEUE);
        Task task = new Task(s3.bucketName, numOfWorkers, "resources" + localAppId, localAppId);
        sqsOPS.SendMessage(task.toString(), L2M_QUEUE_URL); // sent a message with the Task to L2M_Queue


    }


    /*pdfConverter worker = new pdfConverter("/home/maor/Desktop/NLA.pdf");
        worker.readPDF("ToText");
        worker.readPDF("ToHTML");
        worker.readPDF("ToImage");*/
}
