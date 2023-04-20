package com.andersentask.bookshop.user.repository;

import com.andersentask.bookshop.user.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Deprecated
public class UserRepository {

    private final List<User> users;

    private Long id;

    public UserRepository() {
        this.id = 1L;
        this.users = new ArrayList<>();
    }

    public User save(User user) {
        user.setId(id++);
        users.add(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
