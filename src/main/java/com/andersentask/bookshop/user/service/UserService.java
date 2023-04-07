package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.UserCollectionRepositoryImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserCollectionRepositoryImpl userRepository;

    public boolean registration(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            System.out.println("User already exist with email:" + user.getEmail()); // todo: replace with logger
            return false;
        }
        userRepository.save(user);
        System.out.println("Successfully registered! Your email: " + user.getEmail() + "; password: " + user.getPassword()); // todo: replace with logger
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User is missing with email:"));
    }

}

