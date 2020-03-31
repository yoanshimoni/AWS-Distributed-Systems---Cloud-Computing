package manager;

public class Manager {

    static final String L2M_QUEUE = "L2M_Queue";
    static final String W2M_QUEUE = "WorkerToManager";
    static final String M2W_QUEUE = "ManagerToWorker";
    static final String M2W_VISIBILITY_TIMEOUT = "300";


    private static QueueConnection connectionToLocalApp;

    public static void main(String[] args) {

        // Create SQS connections to queues
        connectionToLocalApp = new QueueConnection(L2M_QUEUE, new LocalListner());
        connectionToLocalApp.start();
    }
}
