package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.common.CollectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements CollectionRepository<Book, Long> {
    private final List<Book> books;
    private Long id;

    public BookRepository() {
        this.books = new ArrayList<>();
        this.id = 1L;
    }

    @Override
    public Book save(Book obj) {
        obj.setId(id++);
        books.add(obj);
        return obj;
    }

    @Override
    public void delete(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    @Override
    public Optional<Book> findById(Long id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return this.books;
    }

}
