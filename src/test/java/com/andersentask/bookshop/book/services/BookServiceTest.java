package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
        bookService = new BookService(bookRepository);
    }

    private final Book testBook = Book.builder()
            .name("qwerty")
            .status(BookStatus.AVAILABLE)
            .price(BigDecimal.valueOf(12.12))
            .build();

    private final Book testBook2 = Book.builder()
            .name("qwerty2")
            .status(BookStatus.OUT_OF_STOCK)
            .price(BigDecimal.valueOf(22.22))
            .build();

    private final Book testBook3 = Book.builder()
            .name("qwerty3")
            .status(BookStatus.OUT_OF_STOCK)
            .price(BigDecimal.valueOf(33.33))
            .build();

    @Test
    void save() {
        bookService.save(testBook);

        assertNotNull(testBook.getId());
        assertEquals(Optional.of(testBook), bookService.getBookById(testBook.getId()));
        assertEquals(1, bookService.getAllBooks().size());
        assertTrue(bookRepository.findAll().contains(testBook));
    }

    @Test
    void getAllBooks() {
        bookService.save(testBook);
        bookService.save(testBook2);
        List<Book> bookList = bookService.getAllBooks();

        assertEquals(2, bookService.getAllBooks().size());
        assertTrue(bookList.contains(testBook));
        assertTrue(bookList.contains(testBook2));
    }

    @Test
    void getBookById() {
        bookService.save(testBook);
        Optional<Book> result = bookService.getBookById(testBook.getId());

        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());
        assertEquals(Optional.empty(), bookService.getBookById(2L));
    }

    @Test
    void setStatusToBook() {
        bookService.save(testBook);
        bookService.setStatusToBook(testBook.getId(), BookStatus.OUT_OF_STOCK);

        assertEquals(BookStatus.OUT_OF_STOCK, testBook.getStatus());
        assertNotNull(testBook.getId());
        assertEquals(Optional.of(testBook), bookService.getBookById(testBook.getId()));
        assertTrue(bookRepository.findAll().contains(testBook));
    }

    @Test
    void getSortedBooks() {
        bookService.save(testBook);
        bookService.save(testBook2);

        bookService.getSortedBooks(BookSort.ID);
        assertEquals(1L, bookService.getAllBooks().get(0).getId());
        assertEquals(2L, bookService.getAllBooks().get(1).getId());

        bookService.getSortedBooks(BookSort.NAME);
        assertEquals("qwerty", bookService.getAllBooks().get(0).getName());
        assertEquals("qwerty2", bookService.getAllBooks().get(1).getName());

        bookService.getSortedBooks(BookSort.PRICE);
        assertEquals(BigDecimal.valueOf(12.12), bookService.getAllBooks().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(22.22), bookService.getAllBooks().get(1).getPrice());

        bookService.getSortedBooks(BookSort.STATUS);
        assertEquals(BookStatus.AVAILABLE, bookService.getAllBooks().get(0).getStatus());
        assertEquals(BookStatus.OUT_OF_STOCK, bookService.getAllBooks().get(1).getStatus());
    }

    @Test
    void getBooksByIds() {
        bookService.save(testBook);
        bookService.save(testBook2);
        List<Book> bookList = bookService.getAllBooks();
        List<Long> ids = bookRepository.findAll().stream().map(Book::getId).toList();

        assertEquals(bookList, bookService.getBooksByIds(ids));
    }

    @Test
    void getBooksOutOfStock() {
        bookService.save(testBook);
        bookService.save(testBook2);
        bookService.save(testBook3);
        List<Book> bookList = bookService.getAllBooks();

        assertEquals(bookService.getAllBooks().subList(1, 3), bookService.getBooksOutOfStock(bookList));
    }

    @Test
    void allBooksAreAvailable() {
        bookService.save(testBook);
        bookService.save(testBook2);
        bookService.save(testBook3);
        List<Book> bookList = bookService.getAllBooks();

        bookService.getAllBooks();
        assertFalse(bookService.allBooksAreAvailable(bookList));
    }
}