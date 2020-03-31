package Local;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
// snippet-end:[s3.java2.list_objects.import]

public class ListObjects {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please specify a bucket name");
            System.exit(1);
        }

        // snippet-start:[s3.java2.list_objects.main]
        String bucketName = args[0];

        try {
            Region region = Region.US_EAST_1;
            S3Client s3 = S3Client.builder().region(region).build();

            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();

            for (S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());

            }
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }


}