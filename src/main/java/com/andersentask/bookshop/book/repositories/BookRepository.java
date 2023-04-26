package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
