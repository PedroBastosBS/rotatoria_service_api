package br.com.rotatoria.api.domain.service;

import br.com.rotatoria.api.domain.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    @Value("${amazonProperties.queue-url}")
    private String queueUrl;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendVideoMessage(Video video) {
        String messageBody = String.format("{\"id\":%d,\"filename\":\"%s\"}", video.getId(), video.getFileName());

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        sqsClient.sendMessage(request);
    }
}
