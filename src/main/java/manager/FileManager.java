package manager;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

    private final String QUEUE_NAME_FORMAT = "ManagerTo%s";
    private final String SUMMARY_FILE_FORMAT = "%s-summary";
    private ConcurrentHashMap<String, SummaryFile> files;
    S3BucketOps s3 = new S3BucketOps();

    public FileManager() {
        this.files = new ConcurrentHashMap<>();
        s3.CreateClient();
    }

    /**
     *
     * @param filename - filename is the key of the task we send
     * @param localAppId - the local app id of the local app
     */
    public void addFile(String filename, String localAppId) {
        String summaryFilename = String.format(SUMMARY_FILE_FORMAT, filename);
        File summaryFile = new File(summaryFilename);
        // Add file to map
        files.putIfAbsent(filename, new SummaryFile(summaryFile, summaryFilename, localAppId));
    }

    public void setNumOfImagesInFile(String filename, int numOfImages) {
        SummaryFile summaryFile = files.get(filename);
        if (summaryFile == null) {
            System.err.println("File manager does not contain file: " + filename);
            return;
        }
        summaryFile.setNumOfImages(numOfImages);
    }

    public void appendToFile(String filename, String data) {
        SummaryFile summaryFile = files.get(filename);

        if (summaryFile == null) {
            System.err.println("File manager does not contain file: " + filename);
            return;
        }

        // No more work to be done for this file
        synchronized (summaryFile) {
            summaryFile.addPDFTaskResult(data);
            if (summaryFile.isDone()) {
                summaryFile.createSummaryFile();
                // Create S3 bucket for summary file and upload file
                String bucketName = summaryFile.getLocalAppId() + "-summary";

                this.s3.createBucketV2(bucketName);
                this.s3.uploadFileV2(summaryFile.getFile(), bucketName, summaryFile.getFilename());


              /*  // Create DoneTask to send to local app
                DoneTask doneTask = new DoneTask(bucketName, summaryFile.getFilename());

                // Create SQS queue if not exists
                String queueName = String.format(QUEUE_NAME_FORMAT, summaryFile.getLocalAppId());
                SQSHelper.createSQSQueue(dsps.ass1.manager.Manager.M2Lsqs, queueName);
                String queueUrl = dsps.ass1.manager.Manager.M2Lsqs.getQueueUrl(queueName).getQueueUrl();

                // Send SQS message to local app
                SQSHelper.sendSQSMessage(dsps.ass1.manager.Manager.M2Lsqs, queueUrl, doneTask.toString());*/

                // Remove file from file system
                summaryFile.getFile().delete();
                files.remove(filename);
            }
        }

        /*if (files.size() == 0) {
            // No more work to be done -- turn off all the workers
            System.out.println("No more work to be done; terminating all workers.");
            synchronized (dsps.ass1.manager.MonitorWorkers.lock) {
                dsps.ass1.manager.MonitorWorkers.workersNeeded = 0;
            }
            dsps.ass1.manager.EC2Helper.terminateAllWorkers(dsps.ass1.manager.Manager.ec2);
        }*/

    }

    public int size() {
        return files.size();
    }

    @Override
    public String toString() {
        return files.toString();
    }
}
