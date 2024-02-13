package com.littlebird.searchservice.service;


import com.littlebird.searchservice.constants.ElasticSearch;
import com.littlebird.searchservice.dto.SearchRequest;
import com.littlebird.searchservice.dto.SearchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public Iterable<SearchResponse> getSearchResult(SearchRequest searchRequest) {
        String field = searchRequest.getField();
        String value = searchRequest.getValue();
        Query query = NativeQuery.builder().withQuery(q -> q.wildcard(wc -> wc.field(field).value(value))).build();

        SearchHits<SearchResponse> searchHits =
                elasticsearchOperations.search(query, SearchResponse.class, IndexCoordinates.of(ElasticSearch.POST_INDEX));

        log.info("Number of search hits are : {}", searchHits.getTotalHits());
        return searchHits.stream().map(SearchHit::getContent).toList();

    }

}
