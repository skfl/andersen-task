package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> setStatusToBook(Long id, BookStatus bookStatus) {
        Optional<Book> optionalBook = getBookById(id);
        optionalBook.ifPresent(book -> {
            if (book.getStatus() != bookStatus) {
                book.setStatus(bookStatus);
            }
        });
        return optionalBook;
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        return bookRepository.getSortedBooks(bookSort);
    }

    public List<Book> getBooksByIds(List<Long> ids) {
        List<Book> books = new ArrayList<>();
        for (Long id : ids) {
            getBookById(id).ifPresent(books::add);
        }
        return books;
    }

    public List<Book> getBooksOutOfStock(List<Book> books) {
        return books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
    }

    public boolean allBooksAreAvailable(List<Book> books) {
        return books.stream()
                .allMatch(x -> x.getStatus().equals(BookStatus.AVAILABLE));
    }
}
