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
    static final int DISTRIBUTION_THREADS = 5;


    private static QueueConnection connectionToLocalApp;
    private static QueueConnection connectionToWorkers;


    public static void main(String[] args) {

        // Create SQS connections to queues

        connectionToLocalApp = new QueueConnection(L2M_QUEUE, new LocalListener());
        connectionToLocalApp.start();
        downloadPool = Executors.newFixedThreadPool(DOWNLOAD_THREADS);
        distributionPool = Executors.newFixedThreadPool(DISTRIBUTION_THREADS);

        exit(0);
    }

    private static void exit(int i) {
    }
}
