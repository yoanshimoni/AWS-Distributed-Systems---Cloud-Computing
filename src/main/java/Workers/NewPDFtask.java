package Workers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewPDFtask {
    private String Operation;
    private String URL;
    private int PDFPerWorker;


    @JsonCreator
    public NewPDFtask(@JsonProperty("operation") String operation, @JsonProperty("url") String URL
            , @JsonProperty("pdfperWorker") int PDFPerWorker) {
        this.Operation = operation;
        this.URL = URL;
        this.PDFPerWorker = PDFPerWorker;
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
