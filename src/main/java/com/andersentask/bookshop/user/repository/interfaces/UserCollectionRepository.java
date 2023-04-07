package com.andersentask.bookshop.user.repository.interfaces;

import com.andersentask.bookshop.common.AbstractCollectionRepository;
import com.andersentask.bookshop.user.entities.User;

import java.util.Optional;

public interface UserCollectionRepository extends AbstractCollectionRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
}
