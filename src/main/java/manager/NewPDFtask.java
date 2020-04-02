package manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewPDFtask {
    private String Operation;
    private String URL;


    @JsonCreator
    public NewPDFtask(@JsonProperty("operation") String operation, @JsonProperty("URL") String URL) {
        this.Operation = operation;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public String getOperation() {
        return Operation;
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
