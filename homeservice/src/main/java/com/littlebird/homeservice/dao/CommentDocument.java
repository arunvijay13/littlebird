package com.littlebird.homeservice.dao;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_comment")
public class CommentDocument {

    @Id
    private String id;

    private String comment;

}
