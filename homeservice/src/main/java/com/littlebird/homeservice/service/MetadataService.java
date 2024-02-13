package com.littlebird.homeservice.service;

import com.littlebird.homeservice.dao.CommentDocument;
import com.littlebird.homeservice.dao.Person;
import com.littlebird.homeservice.dao.PostDocument;
import com.littlebird.homeservice.dto.FollowRequest;
import com.littlebird.homeservice.dto.PostUpdateRequest;
import com.littlebird.homeservice.dto.RemoveRequest;
import com.littlebird.homeservice.mapper.PostMapper;
import com.littlebird.homeservice.repository.CommentDocumentRepository;
import com.littlebird.homeservice.repository.PostDocumentRespository;
import com.littlebird.homeservice.repository.UserNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class MetadataService {

    @Autowired
    private PostDocumentRespository postDocumentRespository;

    @Autowired
    private CommentDocumentRepository commentDocumentRepository;

    @Autowired
    private UserNodeRepository userNodeRepository;

    public boolean updateUpvote(PostUpdateRequest postUpdateRequest) {
        Optional<PostDocument> optionalPostDocument = getPostDocument(postUpdateRequest.getId());
        if(optionalPostDocument.isEmpty())
            return false;
        PostDocument postDocument = optionalPostDocument.get();
        if(postDocument.getIsUserUpvoted())
            return false;
        if(postDocument.getIsUserDownvoted()) {
            postDocument.setDownvote(postDocument.getDownvote().subtract(BigInteger.ONE));
        }
        postDocument.setUpvote(postDocument.getUpvote().add(BigInteger.ONE));
        postDocument.setIsUserUpvoted(true);
        postDocument.setIsUserDownvoted(false);
        postDocumentRespository.save(postDocument);
        return true;
    }

    public boolean updateDownvote(PostUpdateRequest postUpdateRequest) {
        Optional<PostDocument> optionalPostDocument = getPostDocument(postUpdateRequest.getId());
        if(optionalPostDocument.isEmpty())
            return false;
        PostDocument postDocument = optionalPostDocument.get();
        if(postDocument.getIsUserDownvoted())
            return false;
        if(postDocument.getIsUserUpvoted()) {
            postDocument.setUpvote(postDocument.getUpvote().subtract(BigInteger.ONE));
        }
        postDocument.setDownvote(postDocument.getUpvote().subtract(BigInteger.ONE));
        postDocument.setIsUserUpvoted(false);
        postDocument.setIsUserDownvoted(true);
        postDocumentRespository.save(postDocument);
        return true;
    }

    public boolean updateShare(PostUpdateRequest postUpdateRequest) {
        Optional<PostDocument> optionalPostDocument = getPostDocument(postUpdateRequest.getId());
        if(optionalPostDocument.isEmpty())
            return false;
        PostDocument postDocument = optionalPostDocument.get();
        postDocument.setShare(postDocument.getShare().add(BigInteger.ONE));
        postDocumentRespository.save(postDocument);
        return true;
    }

    public boolean updateViews(PostUpdateRequest postUpdateRequest) {
        Optional<PostDocument> optionalPostDocument = getPostDocument(postUpdateRequest.getId());
        if(optionalPostDocument.isEmpty())
            return false;
        PostDocument postDocument = optionalPostDocument.get();
        postDocument.setViews(postDocument.getViews().add(BigInteger.ONE));
        postDocumentRespository.save(postDocument);
        return true;
    }

    public boolean addComment(PostUpdateRequest postUpdateRequest) {
        Optional<PostDocument> optionalPostDocument = getPostDocument(postUpdateRequest.getId());
        if(optionalPostDocument.isEmpty())
            return false;
        CommentDocument userComment = PostMapper.getUserComment((String) postUpdateRequest.getValue());
        userComment = commentDocumentRepository.save(userComment);
        PostDocument postDocument = optionalPostDocument.get();
        postDocument.getCommentIds().add(userComment.getId());
        postDocumentRespository.save(postDocument);
        return true;
    }

    public boolean addFollowing(FollowRequest followRequest) {
        Person follower = userNodeRepository.findById(followRequest.follower()).orElse(null);
        Person followed = userNodeRepository.findById(followRequest.followed()).orElse(null);

        if(follower != null && followed != null) {
            follower.addFollows(followed);
            userNodeRepository.save(follower);
            return true;
        }
        return false;
    }

    public boolean removeFollowing(RemoveRequest removeRequest) {
        Person follower = userNodeRepository.findById(removeRequest.follower()).orElse(null);
        Person followed = userNodeRepository.findById(removeRequest.followed()).orElse(null);

        if(follower != null && followed != null) {
            userNodeRepository.removeRelationship(follower.getUsername(), follower.getUsername());
            return true;
        }
        return false;
    }

    private Optional<PostDocument> getPostDocument(String id) {
        return postDocumentRespository.findById(id);
    }

}
