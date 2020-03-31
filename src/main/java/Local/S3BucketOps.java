package Local;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

public class S3BucketOps {

    protected String path;
    String bucketName = "bucket" + System.currentTimeMillis();

    public S3BucketOps(String path) {
        this.path = path;
        createBucket(this.path);
    }

    public void createBucket(String path) {

        File resources = new File(path);
        String key = "resources";
        String amiId = "ami-b66ed3de";
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client
                .builder()
                .region(region)
                .build();

        System.out.printf("bucket name is %s", this.bucketName);

        s3.createBucket(CreateBucketRequest
                .builder()
                .bucket(bucketName)
                .createBucketConfiguration(
                        CreateBucketConfiguration.builder()
                                .build())
                .build());

        // List buckets
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().forEach(x -> System.out.println(x.name()));

        uploadFile(resources, s3, this.bucketName, key);
        // Delete empty bucket

        //printing bucket contents

   /*     DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket).build();
        s3.deleteBucket(deleteBucketRequest);*/

    }

    private void uploadFile(File file, S3Client s3, String bucket, String key) {
        s3.putObject(PutObjectRequest
                        .builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromFile(file));
    }
}

