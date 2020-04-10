package Workers;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class HandleTask implements Runnable {

    private NewPDFtask newPDFtask;
    private String name_for_debug;
    private String instanceId;
    private DonePDFTask donePDFTask;

    public HandleTask(NewPDFtask newPDFtask, String instanceId) {
        this.newPDFtask = newPDFtask;
        this.instanceId = instanceId;
        donePDFTask = new DonePDFTask(newPDFtask.getOperation(), newPDFtask.getURL(), instanceId, "");
    }

    @Override
    public void run() {
        try {
            String fileName = DownloadPDF(newPDFtask);
            this.name_for_debug = fileName;
            pdfConverter pdfConverter = new pdfConverter(fileName);
            final String newFileName = pdfConverter.readPDF(newPDFtask.getOperation());
            UploadToS3(newFileName);
            this.donePDFTask.setResult(newFileName);
        } catch (Exception e) {
//            System.out.printf("failed to convert %s\n", name_for_debug);
            this.donePDFTask.setResult("failed to convert " + name_for_debug);
        }

        Worker.worker_sqsOPS.SendMessage(this.donePDFTask.toString());

    }

    private String DownloadPDF(NewPDFtask newPDFtask) throws MalformedURLException {
        URL url = new URL(newPDFtask.getURL());
        String name = Paths.get(url.getPath()).getFileName().toString();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(newPDFtask.getURL()).openStream());
             FileOutputStream fileOS = new FileOutputStream(name)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            System.out.printf("Successfully downloaded %s\n", name);
        } catch (IOException e) {
//            System.out.printf("failed to download %s\n", name);
            this.donePDFTask.setResult("failed to download " + name);
        }
        return name;
    }

    private void UploadToS3(String fileName) {
        System.out.printf("uploading %s\n", fileName);
        try {
            S3BucketOps s3BucketOps = new S3BucketOps();
            s3BucketOps.uploadFileV2(instanceId, fileName);
        } catch (Exception e) {
            System.out.printf("Failed to upload %s to s3\n", fileName);
        }
    }


}
