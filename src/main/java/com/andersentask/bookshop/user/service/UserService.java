package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.domain.dto.UserDto;
import com.andersentask.bookshop.user.domain.mapper.UserMapper;
import com.andersentask.bookshop.user.domain.model.User;
import com.andersentask.bookshop.user.repository.UserCollectionRepositoryImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserCollectionRepositoryImpl userRepository;

    public boolean registration(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            System.out.println("User already exist with email:" + user.getEmail());
            return false;
        }
        userRepository.save(user);
        System.out.println("Successfully registered! Your email: " + user.getEmail() + "; password: " + user.getPassword());
        return true;
    }

    public UserDto findByEmail(String email) {
        return UserMapper.entityToDto(userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User is missing with email:")));
    }

}

