/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.shesh.aws;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DryRunResult;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.s3.model.Region;

/**
 * Starts or stops and EC2 instance
 */
public class StartStopInstance
{
    public static void startInstance(String instance_id)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();


        DryRunSupportedRequest<StartInstancesRequest> dry_request =
                () -> {
                    StartInstancesRequest request = new StartInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        DryRunResult dry_response = ec2.dryRun(dry_request);

        if(!dry_response.isSuccessful()) {
            System.out.printf(
                    "Failed dry run to start instance %s", instance_id);

            throw dry_response.getDryRunResponse();
        }

        StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instance_id);

        ec2.startInstances(request);

        System.out.printf("Successfully started instance %s", instance_id);
    }

    public static void stopInstance(String instance_id)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        System.out.println("In Stop Instance !");
        DryRunSupportedRequest<StopInstancesRequest> dry_request =
                () -> {
                    StopInstancesRequest request = new StopInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        DryRunResult dry_response = ec2.dryRun(dry_request);

        if(!dry_response.isSuccessful()) {
            System.out.printf(
                    "Failed dry run to stop instance %s", instance_id);
            throw dry_response.getDryRunResponse();
        }

        StopInstancesRequest request = new StopInstancesRequest()
                .withInstanceIds(instance_id);

        ec2.stopInstances(request);

        System.out.printf("Successfully stop instance %s", instance_id);
    }

    public static void main(String[] args)
    {
        final String USAGE =
                "To run this example, supply an instance id and start or stop\n" +
                        "Ex: StartStopInstance <instance-id> <start|stop>\n";

        if (args.length != 2) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String instance_id = args[0];
        boolean start;

        if(args[1].equals("start")) {
            start = true;
        } else {
            start = false;
        }

        System.out.println("Stopping or staring !");
        if(start) {
            startInstance(instance_id);
        } else {
            stopInstance(instance_id);
        }
    }
}
