package com.andersentask.bookshop.user.repository;

import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.interfaces.UserCollectionRepository;

import java.util.List;
import java.util.Optional;

public class UserCollectionRepositoryImpl implements UserCollectionRepository { //todo: implement
    @Override
    public User save(User obj) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findByEmailIgnoreCase(String email) {
        return Optional.empty();
    }
}
