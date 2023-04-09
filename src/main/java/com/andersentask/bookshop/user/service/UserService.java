package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean registration(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

}

