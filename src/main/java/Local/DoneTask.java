package Local;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DoneTask {

    private String bucket;
    private String key;

    @JsonCreator
    public DoneTask(@JsonProperty("bucket") String bucket, @JsonProperty("key") String key) {
        this.bucket = bucket;
        this.key = key;
    }
    public String getBucket() {
        return bucket;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
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
