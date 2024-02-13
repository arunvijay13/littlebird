package com.littlebird.userservice.controller;

import com.littlebird.userservice.contants.ResponseMessage;
import com.littlebird.userservice.model.ChangeUserProfile;
import com.littlebird.userservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @PutMapping("/username")
    public ResponseEntity<Object> changeUsername(@RequestBody ChangeUserProfile changeUserProfile) {
        if(!userProfileService.changeUsername(changeUserProfile))
            return new ResponseEntity<>(ResponseMessage.INVALID_USERNAME, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(ResponseMessage.USERNAME_CHANGED);
    }

    @PutMapping("/password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangeUserProfile changeUserProfile) {
        if(!userProfileService.changePassword(changeUserProfile))
            return new ResponseEntity<>(ResponseMessage.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(ResponseMessage.PASSWORD_CHANGED);
    }

    @PutMapping("/email")
    public ResponseEntity<Object> changeEmail(@RequestBody ChangeUserProfile changeUserProfile) {
        if(!userProfileService.changeEmail(changeUserProfile))
            return new ResponseEntity<>(ResponseMessage.EMAIL_CHANGED, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(ResponseMessage.INVALID_EMAIL);
    }

}
