package com.littlebird.searchservice.controller;

import com.littlebird.searchservice.dto.SearchRequest;
import com.littlebird.searchservice.dto.SearchResponse;
import com.littlebird.searchservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping
    public Iterable<SearchResponse> getSearchResult(@RequestBody SearchRequest searchRequest) {
        return searchService.getSearchResult(searchRequest);
    }
}

