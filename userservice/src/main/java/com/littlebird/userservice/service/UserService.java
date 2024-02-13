package com.littlebird.userservice.service;

import com.littlebird.userservice.contants.UserCode;
import com.littlebird.userservice.dao.Person;
import com.littlebird.userservice.dao.UserInfoDAO;
import com.littlebird.userservice.dto.LoginRequest;
import com.littlebird.userservice.dto.LoginResponse;
import com.littlebird.userservice.dto.UserRequest;
import com.littlebird.userservice.model.ErrorBox;
import com.littlebird.userservice.model.UserProvider;
import com.littlebird.userservice.repository.PersonNodeRepository;
import com.littlebird.userservice.repository.UserRepository;
import com.littlebird.userservice.utils.JWTUtils;
import com.littlebird.userservice.validation.UserValidation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserValidation userValidation;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonNodeRepository personNodeRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(UserCode.USERNAME_NOT_EXIST.getMessage()));
    }

    public List<ErrorBox> createUser(UserRequest userRequest) {
        List<ErrorBox> errorBoxes = new ArrayList<>();

        if (userValidation.checkUsernameExist(userRequest.getUsername())) {
            log.error("Failed to create user : {}", UserCode.USERNAME_EXIST.getMessage());
            errorBoxes.add(ErrorBox.builder()
                    .errorCode(UserCode.USERNAME_EXIST.name())
                    .errorDescription(UserCode.USERNAME_EXIST.getMessage())
                    .build());
        }

        if (!userValidation.checkValidPassword(userRequest.getPassword())) {
            log.error("Failed to create user : {}", UserCode.PASSWORD_NOT_STRONG.getMessage());
            errorBoxes.add(ErrorBox.builder()
                    .errorCode(UserCode.PASSWORD_NOT_STRONG.name())
                    .errorDescription(UserCode.PASSWORD_NOT_STRONG.getMessage())
                    .build());
        }

        if (!userValidation.checkValidEmail(userRequest.getEmail())) {
            log.error("Failed to create user : {}", UserCode.EMAIL_FORMAT_INVALID.getMessage());
            errorBoxes.add(ErrorBox.builder().errorCode(UserCode.EMAIL_FORMAT_INVALID.name()).
                    errorDescription(UserCode.EMAIL_FORMAT_INVALID.getMessage())
                    .build());
        }

        if(!errorBoxes.isEmpty()) {
            return errorBoxes;
        }

        UserInfoDAO userInfoDAO = UserInfoDAO.builder()
                .username(userRequest.getUsername())
                .password(new BCryptPasswordEncoder().encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .provider(UserProvider.LOCAL)
                .build();

        userRepository.save(userInfoDAO);
        log.info("user successfully created in mongo db");

        Person person = new Person(userInfoDAO.getUsername(), userInfoDAO.getEmail());
        personNodeRepository.save(person);
        log.info("user successfully created in neo4j db");

        return errorBoxes;
    }

    public LoginResponse userValidation(LoginRequest loginRequest) {
        // Create an authentication token with the provided username and password
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        LoginResponse loginResponse = new LoginResponse();

        // Perform authentication using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if(authentication.isAuthenticated()) {
            UserInfoDAO userDetails = (UserInfoDAO) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            loginResponse.setJwtToken(jwtUtils.generateJwtToken(userDetails));
            loginResponse.setMessage(UserCode.LOGIN_SUCCESS.getMessage());
            loginResponse.setCode(UserCode.LOGIN_SUCCESS.name());

            log.info("user successfully authenticated");
        } else {
            loginResponse.setCode(UserCode.LOGIN_FAILED.name());
            loginResponse.setMessage(UserCode.LOGIN_FAILED.getMessage());
            loginResponse.setJwtToken(null);

            log.info("user authentication fails");
        }

        return loginResponse;
    }


}
