package com.andersentask.bookshop.user.service;

import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.andersentask.bookshop.user.enums.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private final User user = User.builder()
            .email("example@gmail.com")
            .password("password")
            .firstName("exampleFirstName")
            .lastName("exampleLastName")
            .role(ROLE_USER)
            .build();

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void registrationNewUser() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        assertTrue(userService.registration(user));
        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registrationExistingUser() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        assertFalse(userService.registration(user));
        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
    }

    @Test
    void findByEmailExistingUser() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
    }

    @Test
    void findByEmailMissingUser() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        assertFalse(userService.findByEmail(user.getEmail()).isPresent());
    }
}