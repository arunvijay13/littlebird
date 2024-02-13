package com.littlebird.apigateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlebird.apigateway.model.TweetRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class PostTweetKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> postTweetKafkaTemplate;

    @Value("${kafka.topic.post}")
    private String postTopic;

    public void sendPostTweet(TweetRequest tweetRequest)  {
        try {
            log.info("tweet request payload : {}", tweetRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(tweetRequest);
            CompletableFuture<SendResult<String, Object>> future =
                    postTweetKafkaTemplate.send(postTopic, UUID.randomUUID().toString(), message);
            future.whenComplete((result,ex)->{
                if (ex == null) {
                    System.out.println("Sent message=["  + tweetRequest +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            tweetRequest + "] due to : " + ex.getMessage());
                }
            });
        } catch (Exception ex) {
            log.error("Failed to process the tweet : {}", ex.getMessage());
            ex.printStackTrace();
        }
    }
}
