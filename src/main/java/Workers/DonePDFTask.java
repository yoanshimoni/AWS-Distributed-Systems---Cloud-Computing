package Workers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DonePDFTask {
    private final String operation;
    private final String URL;
    private String result;
    private String bucketName;
    private String key;
    private String localAppId;


    /*TODO continue to work on this, change the parameters*/

    /**
     * @param operation
     * @param URL
     * @param bucketName
     * @param result     - the string which represents the result
     * @param key        - name of the output file - key
     * @param localAppId - local app identifier
     */
    @JsonCreator
    public DonePDFTask(@JsonProperty("operation") String operation,
                       @JsonProperty("url") String URL,
                       @JsonProperty("bucketName") String bucketName,
                       @JsonProperty("result") String result,
                       @JsonProperty("key") String key,
                       @JsonProperty("localAppId") String localAppId) {
        this.operation = operation;
        this.URL = URL;
        this.bucketName = bucketName;
        this.result = result;
        this.key = key;
        this.localAppId = localAppId;
    }

    public String getURL() {
        return URL;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

    public String getKey() {
        return key;
    }

    public String getLocalAppId() {
        return localAppId;
    }

    public void setResult(boolean failed, String Key) {
        if (failed) {
            this.result = Key;
        } else {
            this.result = "https://" + bucketName + ".s3.amazonaws.com/" + Key;
        }
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