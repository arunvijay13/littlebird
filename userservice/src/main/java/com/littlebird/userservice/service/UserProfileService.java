package com.littlebird.userservice.service;

import com.littlebird.userservice.dao.UserInfoDAO;
import com.littlebird.userservice.model.ChangeUserProfile;
import com.littlebird.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    UserRepository userRepository;

    public boolean changeUsername(ChangeUserProfile changeUserProfile) {
        UserInfoDAO userInfoDAO = findUsername(changeUserProfile.userId());
        if(userInfoDAO != null) {
            userInfoDAO.setUsername(changeUserProfile.profileInfo());
            userRepository.save(userInfoDAO);
            return true;
        }
        return false;
    }

    public boolean changeEmail(ChangeUserProfile changeUserProfile) {
        UserInfoDAO userInfoDAO = findUsername(changeUserProfile.userId());
        if(userInfoDAO != null) {
            userInfoDAO.setEmail(changeUserProfile.profileInfo());
            userRepository.save(userInfoDAO);
            return true;
        }
        return false;
    }

    public boolean changePassword(ChangeUserProfile changeUserProfile) {
        UserInfoDAO userInfoDAO = findUsername(changeUserProfile.userId());
        if(userInfoDAO != null) {
            userInfoDAO.setPassword(changeUserProfile.profileInfo());
            userRepository.save(userInfoDAO);
            return true;
        }
        return false;
    }

    private UserInfoDAO findUsername(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

}
