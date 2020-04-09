package manager;

public class SendNewPDFtask implements Runnable {
    private String Operation;
    private String URL;
    private int PDFPerWorker;
    private String M2W_QUEUE_URL;
    private Manager_sqsOPS manager_SQS;

    public SendNewPDFtask(String M2W_QUEUE_URL, String operation, String URL, int PDFPerWorker) {
        this.M2W_QUEUE_URL = M2W_QUEUE_URL;
        Operation = operation;
        this.URL = URL;
        this.PDFPerWorker = PDFPerWorker;
        manager_SQS = new Manager_sqsOPS();


    }

    @Override
    public void run() {
        this.manager_SQS.SendMessage(new NewPDFtask(this.Operation, this.URL, this.PDFPerWorker).toString(), M2W_QUEUE_URL);
    }
}
