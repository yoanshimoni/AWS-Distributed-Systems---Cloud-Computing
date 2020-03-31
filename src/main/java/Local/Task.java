package Local;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    public String bucketName;
    public int numOfWorkers;

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
        return "Task{" +
                "bucketName='" + bucketName + '\'' +
                ", numOfWorkers=" + numOfWorkers +
                '}';
    }
}
