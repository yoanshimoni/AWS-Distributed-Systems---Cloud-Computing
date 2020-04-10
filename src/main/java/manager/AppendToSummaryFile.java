package manager;

public class AppendToSummaryFile implements Runnable{
    private DonePDFTask donePDFTask;

    public AppendToSummaryFile(DonePDFTask donePDFTask) {
        this.donePDFTask = donePDFTask;
    }

    @Override
    public void run() {
//        Manager.fileManager.appendToFile();
    }
}
