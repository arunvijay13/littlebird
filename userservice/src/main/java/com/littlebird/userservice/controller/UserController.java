package com.littlebird.userservice.controller;

import com.littlebird.userservice.contants.ResponseMessage;
import com.littlebird.userservice.contants.UserCode;
import com.littlebird.userservice.dto.LoginRequest;
import com.littlebird.userservice.dto.LoginResponse;
import com.littlebird.userservice.dto.UserRequest;
import com.littlebird.userservice.model.ErrorBox;
import com.littlebird.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
        List<ErrorBox> errorBoxes = userService.createUser(userRequest);
        if(!errorBoxes.isEmpty())
            return new ResponseEntity<>(errorBoxes, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(ResponseMessage.SUCCESSFULLY_CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> loginHandler(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.userValidation(loginRequest);
        if(loginResponse.getCode().equals(UserCode.LOGIN_FAILED.name()))
            return ResponseEntity.badRequest().body(loginResponse);
        return ResponseEntity.ok(loginResponse);
    }
}
