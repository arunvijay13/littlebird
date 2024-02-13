package com.littlebird.userservice.validation;

import com.littlebird.userservice.contants.UserValidationConstant;
import com.littlebird.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidation {

    @Autowired
    UserRepository userRepository;

    public boolean checkUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkValidEmail(String email) {
        return Pattern.compile(UserValidationConstant.EMAIL_PATTERN).matcher(email).matches();
    }

    public boolean checkValidPassword(String password) {
        return Pattern.compile(UserValidationConstant.PASSWORD_PATTERN).matcher(password).matches();
    }

}
