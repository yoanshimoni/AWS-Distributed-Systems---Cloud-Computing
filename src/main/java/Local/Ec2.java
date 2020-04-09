package Local;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.Base64;
import java.util.List;

public class Ec2 {
    Ec2Client ec2Client = null;

    public Ec2() {
        create();
    }

    public void create() {

        ec2Client = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .build();
        String name = "Manager";
        String amiId = "ami-076515f20540e6e0b";

        if (!checkIfExist(ec2Client)) { // if returned false hence Manager does not exist, so we will create that instance
            String managerScript = "";
            RunInstancesRequest runRequest = RunInstancesRequest.builder()
                    .imageId(amiId)
                    .instanceType(InstanceType.T2_MICRO)
                    .maxCount(1)
                    .minCount(1)
                    .userData(Base64.getEncoder().encodeToString(managerScript.getBytes()))
                    .build();

            RunInstancesResponse response = ec2Client.runInstances(runRequest);

            String instanceId = response.instances().get(0).instanceId();

            Tag tag = Tag.builder()
                    .key("Name")
                    .value(name)
                    .build();

            CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                    .resources(instanceId)
                    .tags(tag)
                    .build();

            try {
                ec2Client.createTags(tagRequest);
                System.out.printf(
                        "Successfully started EC2 instance %s based on AMI %s",
                        instanceId, amiId);

            } catch (Ec2Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            // snippet-end:[ec2.java2.create_instance.main]
            System.out.println("Done!");
        } else {
            System.out.println("Manager already exist");
        }
    }

    public boolean checkIfExist(Ec2Client ec2Client) {
        final String name = "Manager";
        try {
            String nextToken = null;

            do {

                // Create a Filter to find all running instances
                Filter filter = Filter.builder()
                        .name("instance-state-name")
                        .values("running")
                        .build();

                //Create a DescribeInstancesRequest
                DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                        .filters(filter)
                        .build();

                // Find the running instances
                DescribeInstancesResponse response = ec2Client.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        for (Tag x : instance.tags()) {
                            if (x.value().equals(name)) {
                                return true;
                            }
                        }
                    }
                }
                nextToken = response.nextToken();

            } while (nextToken != null);

        } catch (Ec2Exception e) {
            e.getStackTrace();
        }

        return false;
    }

    public String checkForMessage(sqsOPS sqsOPS) {
        try {
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder().
                    queueUrl(sqsOPS.queueUrl)
                    .build();
            List<Message> messages = sqsOPS.sqsClient.receiveMessage(receiveRequest).messages();
            for (Message x : messages) {
                if (x.body().toLowerCase().contains("bucket".toLowerCase())) {
                    return x.body();
                }
            }
            return null;
        } catch (AwsServiceException e) {
            e.printStackTrace();
            return null;
        }

    }

}