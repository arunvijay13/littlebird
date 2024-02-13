package com.littlebird.homeservice.configuration;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.topic.post}")
    private String postTopic;

    @Value("${kafka.topic.metadata}")
    private String metadataTopic;

    @Value("${kafka.partition}")
    private Integer partition;

    @Value("${kafka.replication-factor}")
    private Short replicationFactor;

    @Value("${kafka.bootstrap.url}")
    private String kafkaBootrapUrl;

    @Bean
    public NewTopic createPostTopic() {
        return TopicBuilder.name(postTopic).partitions(partition).replicas(replicationFactor).build();
    }

    @Bean
    public NewTopic createMetadataTopic() {
        return TopicBuilder.name(metadataTopic).partitions(partition).replicas(replicationFactor).build();
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootrapUrl);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "post-request-id");
        return props;
    }
}
