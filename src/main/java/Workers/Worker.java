package Workers;

import manager.QueueConnection;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker {
    static final String M2W_QUEUE = "M2W_Queue"; // manager to workers
    static final String W2M_QUEUE = "W2M_Queue"; // workers to manager
    static QueueConnection connectionToManger;
    static ExecutorService slaves;
    static S3Client s3;
    static Worker_sqsOPS worker_sqsOPS;

    public static void main(String[] args) {
        String instanceId = "worker1" + Calendar.getInstance().getTimeInMillis();
        System.out.println("Started worker");
        init(instanceId);
        slaves = Executors.newCachedThreadPool();
        connectionToManger = new QueueConnection(M2W_QUEUE, new WorkerListenerToManager(instanceId));
        connectionToManger.start();

    }

    private static void init(String instanceId) {
        Region region = Region.US_EAST_1;
        s3 = S3Client
                .builder()
                .region(region)
                .build();
        S3BucketOps s3BucketOps = new S3BucketOps();
        s3BucketOps.createBucketV2(s3, instanceId);
        // worker_sqsOPS - is set to w2m_queue
        worker_sqsOPS = new Worker_sqsOPS();
        worker_sqsOPS.createSQS(W2M_QUEUE);


    }
}
