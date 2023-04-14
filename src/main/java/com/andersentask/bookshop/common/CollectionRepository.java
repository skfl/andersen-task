package com.andersentask.bookshop.common;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository<T, ID> {
    T save(T obj);

    void delete(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}
