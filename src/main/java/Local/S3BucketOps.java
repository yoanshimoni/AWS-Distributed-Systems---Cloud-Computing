package Local;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

public class S3BucketOps {

    protected String path;
    public StringBuilder stringBuilder = new StringBuilder();
    public String bucketName;
    public String localAppId;
    private String key;
    private S3Client s3;


    // key is the name of the output file
    public S3BucketOps(String path, String localAppId, String key) {
        this.path = path;
        this.localAppId = localAppId;
        stringBuilder.append("bucket");
        stringBuilder.append(localAppId);
        bucketName = stringBuilder.toString();
        this.key = key + localAppId;
        Region region = Region.US_EAST_1;
        this.s3 = S3Client
                .builder()
                .region(region)
                .build();
        createBucket(this.path);
    }


    public S3BucketOps(String bucketName, String key) {
        this.bucketName = bucketName;
        this.key = key;
        Region region = Region.US_EAST_1;
        this.s3 = S3Client
                .builder()
                .region(region)
                .build();
    }

    public void createBucket(String path) {
        File resources = new File(path);
        System.out.printf("bucket name is %s\n", this.bucketName);
        this.s3.createBucket(CreateBucketRequest
                .builder()
                .bucket(bucketName)
                .createBucketConfiguration(
                        CreateBucketConfiguration.builder()
                                .build())
                .build());
        uploadFile(resources, this.bucketName, this.key);

    }

    private void uploadFile(File file, String bucket, String key) {
        this.s3.putObject(PutObjectRequest
                        .builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromFile(file));
        System.out.printf("uploaded %s to s3\n", key);
    }

    public void downloadSummaryFile() {
        this.s3.getObject(GetObjectRequest.builder().bucket(this.bucketName).key(this.key).build(),
                ResponseTransformer.toFile(Paths.get(this.key)));
        System.out.printf("Downloaded %s\n", this.key);
    }

    public void uploadManagerJarFile(String jarPath) {
        File jar = new File(jarPath);
        this.s3.putObject(PutObjectRequest
                        .builder()
                        .bucket(this.bucketName)
                        .key("jar")
                        .build(),
                RequestBody.fromFile(jar));
        System.out.printf("uploaded %s to s3\n", jar);

    }

}

