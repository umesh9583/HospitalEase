package com.hms.service;

import com.hms.entity.User;
import com.hms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password, String role) {
        User user = userRepository.findByUsernameAndRole(username, role);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
