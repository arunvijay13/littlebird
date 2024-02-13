package com.littlebird.homeservice.dao;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_post")
public class    PostDocument {

    private String id;

    private String username;

    private String message;

    private String url;

    private BigInteger upvote;

    private BigInteger downvote;

    private BigInteger views;

    private BigInteger share;

    private List<String> commentIds;

    private Timestamp timestamp;

    private Boolean isUserUpvoted;

    private Boolean isUserDownvoted;
}
