package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.common.AbstractCollectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements AbstractCollectionRepository<Book, Long> {
    private final List<Book> books;
    private Long id;

    public BookRepository() {
        this.id = 1L;
        this.books = new ArrayList<>();
    }

    @Override
    public Book save(Book obj) {
        obj.setId(id++);
        books.add(obj);
        return obj;
    }

    @Override
    public void delete(Long id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                books.remove(book);
            }
        }
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

    public Book update(Book book) {
        return null;
    }
}
