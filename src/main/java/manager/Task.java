package manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Task {
    private String bucketName;
    private int numOfWorkers;

    @JsonCreator
    public Task(@JsonProperty("bucketName") String bucketName,
                @JsonProperty("numOfWorkers") int numOfWorkers) {
        this.bucketName = bucketName;
        this.numOfWorkers = numOfWorkers;
    }

    public String getBucketName() {
        return bucketName;
    }

    public int getNumOfWorkers() {
        return numOfWorkers;
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
