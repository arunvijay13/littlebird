package com.littlebird.homeservice.repository;

import com.littlebird.homeservice.dao.PostDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDocumentRespository extends MongoRepository<PostDocument, String> {
}
