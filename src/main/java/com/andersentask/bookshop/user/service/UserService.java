package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public boolean registration(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            log.info("User already exists with email: {}", user.getEmail());
            return false;
        }
        userRepository.save(user);
        log.info("Successfully registered! Your email: {}; password: {}", user.getEmail(), user.getPassword());
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User is missing with email:" + email));
    }

}

