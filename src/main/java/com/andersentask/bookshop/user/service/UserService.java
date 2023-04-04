package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andersentask.bookshop.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public boolean registration(User user) {
        if (ifUserExist(user.getEmail())) {
            System.out.println("User already exist with email:" + user.getEmail());
            return false;
        }
        userRepository.save(user);
        System.out.println("Successfully registered! Your email: " + user.getEmail() + "; password: " + user.getPassword());
        return true;
    }

    @Transactional
    public boolean ifUserExist(String userEmail) {
        return userRepository.findByEmailIgnoreCase(userEmail).isPresent();
    }
}
