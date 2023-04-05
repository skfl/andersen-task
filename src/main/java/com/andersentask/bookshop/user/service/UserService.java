package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.domain.dto.UserDto;
import com.andersentask.bookshop.user.domain.mapper.UserMapper;
import com.andersentask.bookshop.user.domain.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andersentask.bookshop.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public boolean registration(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            System.out.println("User already exist with email:" + user.getEmail());
            return false;
        }
        userRepository.save(user);
        System.out.println("Successfully registered! Your email: " + user.getEmail() + "; password: " + user.getPassword());
        return true;
    }

    @Transactional
    public UserDto findByEmail(String email) {
        return userMapper.entityToDto(userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("User is missing with email:")));
    }

}

