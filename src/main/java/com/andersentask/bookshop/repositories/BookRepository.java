package com.andersentask.bookshop.repositories;

import com.andersentask.bookshop.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
