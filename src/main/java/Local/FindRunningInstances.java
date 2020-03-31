package Local;

// snippet-comment:[These are tags for the AWS doc team's sample catalog. Do not remove.]
// snippet-sourcedescription:[FindRunningInstances.java demonstrates how to use a Filter to find running instances]
// snippet-service:[ec2]
// snippet-keyword:[Java]
// snippet-keyword:[Code Sample]
// snippet-sourcetype:[full-example]
// snippet-sourcedate:[2020-01-10]
// snippet-sourceauthor:[AWS-scmacdon]

/**
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p>
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 * <p>
 * http://aws.amazon.com/apache2.0/
 * <p>
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

// snippet-start:[ec2.java2.running_instances.complete]


// snippet-start:[ec2.java2.running_instances.import]

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
// snippet-end:[ec2.java2.running_instances.import]

/**
 * Locates all running EC2 instances using a Filter
 */
public class FindRunningInstances {
    public static void main(String[] args) {

        Ec2Client ec2Client = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .build();
        String amiId = "ami-076515f20540e6e0b";
        // snippet-start:[ec2.java2.running_instances.main]

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
                        instance.tags().forEach(x-> System.out.println(x.value()));
                    }
                }
                nextToken = response.nextToken();

            } while (nextToken != null);

        } catch (Ec2Exception e) {
            e.getStackTrace();
        }

        // snippet-end:[ec2.java2.running_instances.main]
    }
}
// snippet-end:[ec2.java2.running_instances.co