package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
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

    public void setStatusToBook(Long id, BookStatus bookStatus) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find book with such id while updating"));
        bookToUpdate.setStatus(bookStatus);
        bookRepository.save(bookToUpdate);
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
//        return bookRepository.getSortedBooks(bookSort);
        return null;
    }

    public List<Book> getBooksByIds(List<Long> bookIds) {
        List<Book> books = new ArrayList<>();
        bookIds.stream()
                .map(this::getBookById)
                .forEach(book -> book.ifPresent(books::add));
        return books;
    }

    public List<Book> getBooksOutOfStock(List<Book> books) {
        return books.stream()
                .filter(book -> book.getStatus() == BookStatus.OUT_OF_STOCK)
                .toList();
    }

    public boolean allBooksAreAvailable(List<Book> books) {
        return books.stream()
                .allMatch(book -> book.getStatus() == BookStatus.AVAILABLE);
    }
}
