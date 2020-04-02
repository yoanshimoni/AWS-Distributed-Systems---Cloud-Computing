package manager;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


public class sqsOPS {

    public static String queueUrl = null;
    public static SqsClient sqsClient = null;

    public sqsOPS() {


    }

    public static String createSQS(String QUEUE_NAME) {

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

        } catch (QueueNameExistsException e) {
            throw e;
        }

        return queueUrl;

    }

    public static void SendMessage(String message, String queueUrl) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(sendMsgRequest);
        System.out.printf("sent %s", message);
//        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
//                .queueUrl(queueUrl)
//                .build();
//        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
//        messages.forEach(x -> System.out.printf("message sent:\n%s", x.body()));
    }
}