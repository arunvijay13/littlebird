package com.littlebird.homeservice.controller;

import com.littlebird.homeservice.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private UserPostService userPostService;

//    @PostMapping
//    public ResponseEntity<HttpStatus> createPost(@RequestBody PostRequest postRequest) {
//        //if(userPostService.saveUserPost(postRequest))
//            return ResponseEntity.ok().build();
//        return ResponseEntity.badRequest().build();
//    }
}
