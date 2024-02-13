package com.littlebird.homeservice.controller;

import com.littlebird.homeservice.dto.FollowRequest;
import com.littlebird.homeservice.dto.PostUpdateRequest;
import com.littlebird.homeservice.dto.RemoveRequest;
import com.littlebird.homeservice.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @PutMapping("/upvote")
    public ResponseEntity<HttpStatus> updateUpvote(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(metadataService.updateUpvote(postUpdateRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/downvote")
    public ResponseEntity<HttpStatus> updateDownvote(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(metadataService.updateDownvote(postUpdateRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/view")
    public ResponseEntity<HttpStatus> updateViews(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(metadataService.updateViews(postUpdateRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/share")
    public ResponseEntity<HttpStatus> updateShare(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(metadataService.updateShare(postUpdateRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/comment")
    public ResponseEntity<HttpStatus> AddComment(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(metadataService.addComment(postUpdateRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/follow")
    public ResponseEntity<HttpStatus> addFollower(@RequestBody FollowRequest followRequest) {
        if(metadataService.addFollowing(followRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/follow")
    public ResponseEntity<HttpStatus> removeFollowing(@RequestBody RemoveRequest removeRequest) {
        if(metadataService.removeFollowing(removeRequest))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

}
