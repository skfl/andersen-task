package com.andersentask.bookshop.user.repository;

import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.andersentask.bookshop.user.enums.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    private UserRepository userRepository;

    private final User user = User.builder()
            .firstName("exampleFirstName")
            .lastName("exampleLastName")
            .email("example@gmail.com")
            .password("password")
            .role(ROLE_USER)
            .build();

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void save() {

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(Optional.of(user), userRepository.findByEmailIgnoreCase(user.getEmail()));
        assertEquals(1, userRepository.findAll().size());
        assertTrue(userRepository.findAll().contains(savedUser));
    }

    @Test
    void findByIdExistingUser() {
        userRepository.save(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindByIdMissingUser() {
        Optional<User> result = userRepository.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll() {
        User user2 = User.builder()
                .firstName("exampleFirstName2")
                .lastName("exampleLastName2")
                .email("example2@gmail.com")
                .password("password2")
                .role(ROLE_USER)
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<User> result = userRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user));
        assertTrue(result.contains(user2));
    }

    @Test
    void findByEmailIgnoreCaseExistingUser() {
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmailIgnoreCase(user.getEmail().toUpperCase());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindByEmailIgnoreCaseMissingUser() {
        Optional<User> result = userRepository.findByEmailIgnoreCase("missingemail@gmail.com");

        assertTrue(result.isEmpty());
    }
}