package manager;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

public class CreateWorkers implements Runnable {
    private int numOfWorkers;
    private int workersExists;
    private final int maxWorkers = 10;


    public CreateWorkers(int numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
        this.workersExists = countWorkersExist();


    }

    @Override
    public void run() {

        if (maxWorkers > numOfWorkers + workersExists) {
            Manager_Ec2 ec2OPS = new Manager_Ec2();
            System.out.printf("number of workers to create is %d\n", numOfWorkers);
            System.out.printf("Starting %d additional worker nodes\n", this.numOfWorkers);
//            ec2OPS.createWorkers(Manager.ec2Client, maxWorkers - numOfWorkers - workersExists);
            System.out.println("for now we wont create nothing since its still local");
        } else {
            System.out.println("I will now open more workers since im at my max.");
        }
    }

    private int countWorkersExist() {
        Ec2Client ec2 = ec2 = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .build();
        int counter = 0;
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
                        for (Tag x : instance.tags()) {
                            counter++;
                        }
                    }
                }
                nextToken = response.nextToken();

            } while (nextToken != null);

        } catch (Ec2Exception e) {
            e.getStackTrace();
        }
        return counter;
    }
}

