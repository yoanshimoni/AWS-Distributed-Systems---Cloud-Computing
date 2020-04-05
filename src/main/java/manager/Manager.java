package manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager {

    static final String L2M_QUEUE = "L2M_Queue"; // local to manager
    static final String M2W_QUEUE = "M2W_Queue"; // manager to workers
    static final String W2M_QUEUE = "WorkerToManager";
    static final String M2W_VISIBILITY_TIMEOUT = "300";
    static ExecutorService downloadPool;
    static ExecutorService distributionPool;
    static final int DOWNLOAD_THREADS = 5;
    static final int DISTRIBUTION_THREADS = 300; //we go hard


    private static QueueConnection connectionToLocalApp;
    private static QueueConnection connectionToWorkers;
    private static Manager_sqsOPS manager_SQS;

    public static void main(String[] args) {
        //TODO understand why i cant send message in M2W_QUEUE_URL
        manager_SQS = new Manager_sqsOPS();
        final String M2W_QUEUE_URL = manager_SQS.createSQS(M2W_QUEUE);
//        SqsClient sqsClient = SqsClient.builder()
//                .region(Region.US_EAST_1)
//                .build();
//        GetQueueUrlResponse getQueueUrlResponse =
//                sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(M2W_QUEUE).build());
//        String M2W_QUEUE_URL = getQueueUrlResponse.queueUrl();

        // Create SQS connections to queues

        downloadPool = Executors.newFixedThreadPool(DOWNLOAD_THREADS);
        distributionPool = Executors.newFixedThreadPool(DISTRIBUTION_THREADS);
        connectionToLocalApp = new QueueConnection(L2M_QUEUE, new LocalListener(M2W_QUEUE_URL));
        connectionToLocalApp.start();

        exit(0);
    }

    private static void exit(int i) {
    }
}
