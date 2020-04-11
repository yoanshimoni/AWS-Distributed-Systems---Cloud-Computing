package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class SummaryFile {
    private String filename;
    private String localAppId;
    private File file;
    private int numOfImages;
    private CopyOnWriteArrayList<String> data;

    public SummaryFile(File file, String filename, String localAppId) {
        this.filename = filename;
        System.out.println("SummaryFile - filename: " + this.filename);
        this.localAppId = localAppId;
        System.out.println("localAppId - localAppId: " + this.localAppId);
        this.file = file;
        this.numOfImages = -1;
        this.data = new CopyOnWriteArrayList<>();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocalAppId() {
        return localAppId;
    }

    public void setLocalAppId(String localAppId) {
        this.localAppId = localAppId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
        System.out.printf("set num of images to %s in %s\n", numOfImages, this.filename);

    }

    public void addPDFTaskResult(String data) {
        this.data.add(data);
        System.out.printf("Appended to file %s %s\n",this.filename,data);
    }

    public boolean isDone() {
        return data.size() == numOfImages;
    }

    public int getSize(){
        return data.size();
    }

    public void createSummaryFile() {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            for (String res : this.data)
                out.write(res + "\n");
        } catch (IOException ex) {
            System.err.println("Caught an exception while trying " +
                    "to write to file: " + filename);
            ex.printStackTrace();
        }

    }

}