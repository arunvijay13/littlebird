package com.littlebird.homeservice.repository;

import com.littlebird.homeservice.dao.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDocumentRepository extends MongoRepository<CommentDocument, String> {
}
