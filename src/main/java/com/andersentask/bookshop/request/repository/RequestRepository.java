package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {
    void removeByBook(Book book);
    Long countByBook(Book book);
}
