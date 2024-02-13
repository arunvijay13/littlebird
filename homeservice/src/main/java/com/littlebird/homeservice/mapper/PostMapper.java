package com.littlebird.homeservice.mapper;

import com.littlebird.homeservice.dao.CommentDocument;
import com.littlebird.homeservice.dao.ESPostDocument;
import com.littlebird.homeservice.dao.PostDocument;
import com.littlebird.homeservice.dto.TweetRequest;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class PostMapper {

    public static PostDocument getUserPost(TweetRequest tweetData) {
        return PostDocument
                .builder()
                .username(tweetData.getUsername())
                .message(tweetData.getMessage())
                .url(tweetData.getUrl())
                .upvote(BigInteger.ZERO)
                .downvote(BigInteger.ZERO)
                .views(BigInteger.ZERO)
                .share(BigInteger.ZERO)
                .commentIds(new ArrayList<>())
                .isUserUpvoted(false)
                .isUserDownvoted(false)
                .timestamp(Timestamp.from(Instant.now()))
                .build();
    }

    public static ESPostDocument getESPostDocument(PostDocument postDocument) {
        return ESPostDocument.builder()
                .id(postDocument.getId())
                .username(postDocument.getUsername())
                .message(postDocument.getMessage())
                .build();
    }

    public static CommentDocument getUserComment(String message) {
        return CommentDocument.builder().comment(message).build();
    }
}
