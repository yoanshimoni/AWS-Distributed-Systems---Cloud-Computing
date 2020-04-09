package Workers;

import manager.QueueConnection;

public class Worker {
    static final String M2W_QUEUE = "M2W_Queue"; // manager to workers
    static QueueConnection connectionToManger;

    public static void main(String[] args) {
        System.out.println("Started worker");
        connectionToManger = new QueueConnection(M2W_QUEUE, new WorkerListenerToManager());
        connectionToManger.start();

    }
}
