package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
    }

    private final Book testBook = Book.builder()
            .name("qwerty")
            .status(BookStatus.AVAILABLE)
            .price(BigDecimal.valueOf(12.12))
            .build();

    @Test
    void save() {
        bookRepository.save(testBook);

        assertNotNull(testBook.getId());
        assertEquals(Optional.of(testBook), bookRepository.findById(1L));
        assertEquals(1, bookRepository.findAll().size());
        assertTrue(bookRepository.findAll().contains(testBook));
    }

    @Test
    void findById() {
        bookRepository.save(testBook);
        Optional<Book> result = bookRepository.findById(testBook.getId());

        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());

        assertEquals(Optional.empty(), bookRepository.findById(2L));
    }

    @Test
    void findAll() {
        Book testBook2 = Book.builder()
                .name("qwerty2")
                .status(BookStatus.OUT_OF_STOCK)
                .price(BigDecimal.valueOf(22.22))
                .build();

        bookRepository.save(testBook);
        bookRepository.save(testBook2);

        List<Book> result = bookRepository.findAll();
        assertEquals(2, bookRepository.findAll().size());
        assertTrue(result.contains(testBook));
        assertTrue(result.contains(testBook2));
    }
}