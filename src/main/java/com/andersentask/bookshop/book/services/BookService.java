package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.request.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final RequestService requestService;


    public Optional<Book> getBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public void setStatusToBook(Long id, BookStatus bookStatus) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find book with such id"));
        bookToUpdate.setStatus(bookStatus);
        bookRepository.save(bookToUpdate);
        if (bookStatus == BookStatus.AVAILABLE) {
            requestService.deleteRequest(bookToUpdate);
        }
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC,
                bookSort.toString().toLowerCase(Locale.ROOT)));
    }

    //toDo: re-write with classic loop
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
