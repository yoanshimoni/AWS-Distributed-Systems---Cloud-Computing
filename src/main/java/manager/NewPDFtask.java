package manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewPDFtask {
    private String Operation;
    private String URL;
    private int PDFPerWorker;
    private String key;
    private String localAppId;

    /**
     * @param operation    - operation to execute on the pdf
     * @param URL          - the location of the PDF
     * @param PDFPerWorker - num of pdf per worker
     * @param key          - the key will be used as the summary file name
     * @param localAppId   - the identifier of the local app
     */
    @JsonCreator
    public NewPDFtask(@JsonProperty("operation") String operation, @JsonProperty("url") String URL
            , @JsonProperty("pdfperWorker") int PDFPerWorker, @JsonProperty("key") String key,
                      @JsonProperty("localAppId") String localAppId) {
        this.Operation = operation;
        this.URL = URL;
        this.PDFPerWorker = PDFPerWorker;
        this.key = key;
        this.localAppId = localAppId;
    }

    public String getURL() {
        return URL;
    }

    public String getOperation() {
        return Operation;
    }

    public int getPDFPerWorker() {
        return PDFPerWorker;
    }

    public String getKey() {
        return key;
    }

    public String getLocalAppId() {
        return localAppId;
    }

    @Override
    public String toString() {
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            System.err.println("Caught an exception while parsing to Json String");
            ex.printStackTrace();
        }
        return json;
    }

}
