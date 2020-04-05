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
    static ExecutorService nodesCreationPool;
    static ExecutorService ConvertPDFPool;
    static final int DOWNLOAD_THREADS = 5;
    static final int DISTRIBUTION_THREADS = 200; //we go hard
    static final int CREATION_THREADS = 1;
    static final int CONVERT_THREADS = 10;


    private static QueueConnection connectionToLocalApp;
    private static QueueConnection connectionToWorkers;
    private static Manager_sqsOPS manager_SQS;

    public static void main(String[] args) {
        manager_SQS = new Manager_sqsOPS();
        final String M2W_QUEUE_URL = manager_SQS.createSQS(M2W_QUEUE);
        initialize();
        connectionToLocalApp = new QueueConnection(L2M_QUEUE, new LocalListener(M2W_QUEUE_URL));
        connectionToLocalApp.start();

    }

    private static void initialize() {
        // Create thread pools for manager's tasks
        downloadPool = Executors.newFixedThreadPool(DOWNLOAD_THREADS);
        distributionPool = Executors.newFixedThreadPool(DISTRIBUTION_THREADS);
        nodesCreationPool = Executors.newFixedThreadPool(CREATION_THREADS);
        ConvertPDFPool = Executors.newFixedThreadPool(CONVERT_THREADS);
    }
}
