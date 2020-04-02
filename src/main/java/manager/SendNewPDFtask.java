package manager;

public class SendNewPDFtask implements Runnable {
    private String Operation;
    private String URL;

    public SendNewPDFtask(String operation, String URL) {
        Operation = operation;
        this.URL = URL;

    }

    @Override
    public void run() {
//        sqsOPS.SendMessage();
    }
}
