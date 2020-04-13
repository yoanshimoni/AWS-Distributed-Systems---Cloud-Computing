package Local;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

import java.util.Arrays;
import java.util.List;

public class DeleteBuckets {
    public static void main(String[] args) throws Exception {
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();

        String[] arr = {"maorrockyjars", "bucket0a10fa64-0e79-4ca9-9fb3-34f8bd2d4745", "bucketa983a155-1587-468d-9e7b-56e21a92b3f9"};
        List<String> lst = Arrays.asList(arr);
        // List buckets
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        for (Bucket bucket : listBucketsResponse.buckets()) {
            if (lst.contains(bucket.name())) {
                System.out.println("could not delete " + bucket.name());
                continue;
            }
            System.out.println("Deleting " + bucket.name());
            // To delete a bucket, all the objects in the bucket must be deleted first
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucket.name()).build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket.name()).key(s3Object.key()).build());
                }

                listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucket.name())
                        .continuationToken(listObjectsV2Response.nextContinuationToken())
                        .build();

            } while (listObjectsV2Response.isTruncated());


            // Delete empty bucket
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket.name()).build();
            s3.deleteBucket(deleteBucketRequest);
        }


        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();


        String prefix = "ManagerTo";
        ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(prefix).build();
        ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);
        for (String url : listQueuesResponse.queueUrls()) {
            System.out.println(url);
            DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(url).build();
            sqsClient.deleteQueue(deleteQueueRequest);
        }


    }
}

