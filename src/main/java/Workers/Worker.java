package Workers;

import manager.QueueConnection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker {
    static final String M2W_QUEUE = "M2W_Queue"; // manager to workers
    static QueueConnection connectionToManger;
    static ExecutorService slaves;
    public static void main(String[] args) {
        System.out.println("Started worker");
        connectionToManger = new QueueConnection(M2W_QUEUE, new WorkerListenerToManager());
        connectionToManger.start();
        slaves = Executors.newCachedThreadPool();

    }
}
