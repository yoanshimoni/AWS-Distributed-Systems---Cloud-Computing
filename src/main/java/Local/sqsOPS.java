package Local;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


public class sqsOPS {

    public String queueUrl = null;
    public SqsClient sqsClient = null;

    public sqsOPS() {


    }

    public String createSQS(String QUEUE_NAME) {

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

    public void SendMessage(String message, String queueUrl) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(sendMsgRequest);
        System.out.printf("sent %s\n", message);
//        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
//                .queueUrl(queueUrl)
//                .build();
//        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
//        messages.forEach(x -> System.out.printf("message sent:\n%s", x.body()));
    }
}