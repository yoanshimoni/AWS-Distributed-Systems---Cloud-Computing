package manager;

public class AppendToSummaryFile implements Runnable {
    private DonePDFTask donePDFTask;

    public AppendToSummaryFile(DonePDFTask donePDFTask) {
        this.donePDFTask = donePDFTask;
    }

    @Override
    public void run() {
        String data = donePDFTask.getOperation() + " " + donePDFTask.getURL() + " " + donePDFTask.getResult();
        Manager.fileManager.appendToFile(donePDFTask.getKey(),data);
    }
}
