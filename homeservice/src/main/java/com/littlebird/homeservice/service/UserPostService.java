package com.littlebird.homeservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlebird.homeservice.constants.ElasticSearch;
import com.littlebird.homeservice.dao.ESPostDocument;
import com.littlebird.homeservice.dao.PostDocument;
import com.littlebird.homeservice.dto.TweetRequest;
import com.littlebird.homeservice.mapper.PostMapper;
import com.littlebird.homeservice.repository.PostDocumentRespository;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserPostService {

    @Autowired
    private PostDocumentRespository postDocumentRespository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @KafkaListener(topics = "${kafka.topic.post}", groupId = "post-request-id")
    public boolean saveUserPost(ConsumerRecord<String, String> record){
        try {
            log.info("message received from kafka");
            log.info("tweet request : {}", record.value());
            String message = record.value();
            ObjectMapper objectMapper = new ObjectMapper();
            TweetRequest tweetRequest = objectMapper.readValue(message, TweetRequest.class);
            log.info("message : {}, url : {}, username : {}", tweetRequest.getMessage(),
                    tweetRequest.getUrl(), tweetRequest.getUsername());
            if(record.value() != null) {
                log.info("successfully serialized");
                PostDocument postDocument = PostMapper.getUserPost(tweetRequest);
                postDocument = postDocumentRespository.save(postDocument);
                ESPostDocument esPostDocument = PostMapper.getESPostDocument(postDocument);
                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(postDocument.getId())
                        .withObject(esPostDocument)
                        .build();
                elasticsearchOperations.index(indexQuery, IndexCoordinates.of(ElasticSearch.POST_INDEX));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to create post : {}",  e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
