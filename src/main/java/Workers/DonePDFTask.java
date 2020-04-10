package Workers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DonePDFTask {
    private final String Operation;
    private final String URL;
    private String result;
    private String BucketName;

    /**
     * @param operationnot
     * @param URL
     * @param result       - if the task succeeded than result will be the key(name) of the file.
     *                     else it will be failed
     */
    @JsonCreator
    public DonePDFTask(@JsonProperty("operation") String operation, @JsonProperty("url") String URL
            , @JsonProperty("bucketname") String BucketName, @JsonProperty("result") String result) {
        this.Operation = operation;
        this.URL = URL;
        this.BucketName = BucketName;
        this.result = result;
    }

    public String getURL() {
        return URL;
    }

    public String getBucketName() {
        return BucketName;
    }

    public String getOperation() {
        return Operation;
    }

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
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
