package manager;

import com.amazonaws.AmazonClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class Manager_Ec2 {
    Ec2Client ec2Client = null;
    private static final String PROCESS_KEY = "Process";
    private static final String WORKER_PROCESS_VALUE = "Worker";
    private static final String amiId = "ami-076515f20540e6e0b";
    // worker roll
    private static final String WORKER_ARN = "arn:aws:iam::534542618717:instance-profile/Worker";

    public Manager_Ec2() {
    }

    public void create() {

        ec2Client = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .build();
        String name = "Manager";
        //TODO continue
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



    public Collection<String> getWorkerIds(Ec2Client ec2) {
        Collection<String> res = new ArrayList<String>();
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
                DescribeInstancesResponse response = ec2.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        res.add(instance.instanceId());
                    }
                }
                nextToken = response.nextToken();

            } while (nextToken != null);

        } catch (Ec2Exception e) {
            e.getStackTrace();
        }
        return res;
    }

    public void terminateAllWorkers(Ec2Client ec2) {
        try {
            Collection<String> instanceIds = getWorkerIds(ec2);
            TerminateInstancesRequest terminateInstancesRequest = TerminateInstancesRequest.
                    builder()
                    .instanceIds(instanceIds)
                    .build();
            ec2.terminateInstances(terminateInstancesRequest);
        } catch (AmazonClientException ex) {
            System.err.println("Caught an exception while trying to terminate workers");
            ex.printStackTrace();
        }
        System.out.println("Terminated all workers");
    }

    public int countWorkers(Ec2Client ec2) {
        int count = 0;
        try {
            count = getWorkerIds(ec2).size();
        } catch (AmazonClientException ex) {
            System.err.println("Caught an exception while counting worker instances");
            ex.printStackTrace();
        }
        return count;
    }


    public void createWorkers(Ec2Client ec2, int numOfWorkers) {
        final String WORKER_USER_DATA_SCRIPT = "";

        try {
            RunInstancesRequest runRequest = RunInstancesRequest.builder()
                    .imageId(amiId)
                    .instanceType(InstanceType.T2_MICRO)
                    .maxCount(numOfWorkers)
                    .minCount(numOfWorkers)
                    .userData(Base64.getEncoder().encodeToString(WORKER_USER_DATA_SCRIPT.getBytes()))
                    .build();

            RunInstancesResponse response = ec2.runInstances(runRequest);
            List<Instance> instanceList = response.instances();
            int i = 1;
            for (Instance instance : instanceList) {
                Tag tag = Tag.builder()
                        .key("Name")
                        .value("Worker" + i)
                        .build();

                CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                        .resources(instance.instanceId())
                        .tags(tag)
                        .build();
                ec2.createTags(tagRequest);
                System.out.println("created Worker" + i);
                 i++;
            }


        } catch (AmazonClientException ex) {
            System.err.println("Caught an exception while creating worker instances");
            ex.printStackTrace();
        }
        System.out.printf("%d Worker instances created and started\n", numOfWorkers);
    }
}