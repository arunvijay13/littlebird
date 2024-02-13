package com.littlebird.userservice.service;

import com.littlebird.userservice.dao.Person;
import com.littlebird.userservice.dao.UserInfoDAO;
import com.littlebird.userservice.repository.PersonNodeRepository;
import com.littlebird.userservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonNodeRepository personNodeRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("oauth user request intercepted");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user details from OAuth2User
        String username = oAuth2User.getAttribute("username");
        String email = oAuth2User.getAttribute("email");

        // Check if the user already exists in the database
        Optional<UserInfoDAO> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            // User not found, create a new user in the database
            UserInfoDAO user = new UserInfoDAO();
            user.setId(oAuth2User.getAttribute("sub"));
            user.setUsername(username);
            user.setEmail(email);
            userRepository.save(user);

            Person person = new Person(username, email);
            personNodeRepository.save(person);

            log.info("user successfully created in both mongo and neo4j db");
        }

        return oAuth2User;
    }
}
