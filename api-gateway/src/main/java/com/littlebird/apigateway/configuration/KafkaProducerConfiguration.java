package com.littlebird.apigateway.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

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
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootrapUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
