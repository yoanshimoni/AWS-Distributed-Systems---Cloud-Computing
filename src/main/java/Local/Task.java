package Local;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Task {
    private String bucketName;
    private int numOfWorkers;
    private String key;
    private String localAppId;

    @JsonCreator
    public Task(@JsonProperty("bucketName") String bucketName,
                @JsonProperty("numOfWorkers") int numOfWorkers,
                @JsonProperty("key") String key,
                @JsonProperty("localAppId") String localAppId) {
        this.bucketName = bucketName;
        this.numOfWorkers = numOfWorkers;
        this.key = key;
        this.localAppId = localAppId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public int getNumOfWorkers() {
        return numOfWorkers;
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
