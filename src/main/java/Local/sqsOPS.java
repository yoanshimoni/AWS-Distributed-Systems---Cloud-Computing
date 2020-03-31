package Local;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;


public class sqsOPS {
    public final String QUEUE_NAME = "L2M_Queue";
    public String queueUrl = null;
    public SqsClient sqsClient = null;

    public sqsOPS() {
        createSQS();
    }

    public void createSQS() {

        sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();

        try {
            CreateQueueRequest request = CreateQueueRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();
            CreateQueueResponse createResult = sqsClient.createQueue(request);

            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();

            queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

            /*SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody("hello world")
                    .delaySeconds(5)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);*/

            // Send multiple messages to the queue
            /*SendMessageBatchRequest sendBatchRequest = SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(
                            SendMessageBatchRequestEntry.builder()
                                    .messageBody("Hello from message 1")
                                    .id("msg_1")
                                    .build()
                            ,
                            SendMessageBatchRequestEntry.builder()
                                    .messageBody("Hello from message 2")
                                    .delaySeconds(10)
                                    .id("msg_2")
                                    .build())
                    .build();
            sqsClient.sendMessageBatch(sendBatchRequest);*/

            // receive messages from the queue
           /* ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .build();
            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
            messages.forEach(x-> System.out.println(x.body()));

            // print out the messages
            for (Message m : messages) {
                System.out.println("\n" +m.body());
            }*/
        } catch (QueueNameExistsException e) {
            throw e;
        }


    }

    public void SendMessage(String message) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(sendMsgRequest);

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();
        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
        messages.forEach(x -> System.out.printf("message sent:\n%s", x.body()));
    }
}