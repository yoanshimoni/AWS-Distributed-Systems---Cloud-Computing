package Workers;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class HandleRequest implements Runnable {

    private NewPDFtask newPDFtask;
    private String name_for_debug;
    private String OP_for_debug;

    public HandleRequest(NewPDFtask newPDFtask) {
        this.newPDFtask = newPDFtask;
    }

    @Override
    public void run() {
        try {
            String fileName = DownloadPDF(newPDFtask);
            this.name_for_debug = fileName;
            this.OP_for_debug = newPDFtask.getOperation();
            pdfConverter pdfConverter = new pdfConverter(fileName);
            pdfConverter.readPDF(newPDFtask.getOperation());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("this is from %s \n", name_for_debug);
        }

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
            // handles IO exceptions
        }
        return name;
    }
}
