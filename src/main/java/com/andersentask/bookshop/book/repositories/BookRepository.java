package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.common.CollectionRepository;

import java.util.ArrayList;
import java.util.Comparator;
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
    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Book> findAll() {
        return this.books;
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        switch (bookSort) {
            case NAME -> {
                return books.stream()
                        .sorted(Comparator.comparing(Book::getName))
                        .toList();
            }
            case PRICE -> {
                return books.stream()
                        .sorted(Comparator.comparing(Book::getPrice))
                        .toList();
            }
            case STATUS -> {
                return books.stream()
                        .sorted(Comparator.comparing(x -> x.getStatus().ordinal()))
                        .toList();
            }
            case ID -> {
                return books.stream()
                        .sorted(Comparator.comparing(Book::getId))
                        .toList();
            }
        }
        return books;
    }
}
