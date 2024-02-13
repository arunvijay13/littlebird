package com.littlebird.searchservice.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Log4j2
@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        log.info("Elastic search connection established");
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }
}
