package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookCollectionRepositoryImpl;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.enums.Role;
import com.andersentask.bookshop.user.repository.UserRepository;
import com.andersentask.bookshop.user.service.UserService;

public class BookstoreConsoleAppContextConfig {
    private final BookService bookService;
    private final UserService userService;

    public BookstoreConsoleAppContextConfig() {
        this.bookService = new BookService(new BookCollectionRepositoryImpl());
        this.userService = new UserService(new UserRepository());
        setupBookService();
        setupUserService();
    }

    private void setupBookService() {
        this.bookService.save(Book.builder()
                .price(123.0)
                .name("Book")
                .status(BookStatus.AVAILABLE)
                .build());
        this.bookService.save(Book.builder()
                .price(23.0)
                .name("aBook")
                .status(BookStatus.AVAILABLE)
                .build());
        this.bookService.save(Book.builder()
                .price(1123.0)
                .name("zook")
                .status(BookStatus.NOT_AVAILABLE)
                .build());
        this.bookService.save(Book.builder()
                .price(923.0)
                .name("zwook")
                .status(BookStatus.NOT_AVAILABLE)
                .build());
    }

    private void setupUserService() {
        User admin = User.builder()
                .email("a")
                .firstName("admin")
                .lastName("admin")
                .password("a")
                .role(Role.ROLE_ADMIN)
                .build();
        this.userService.registration(admin);
    }

    public BookService getBookService() {
        return this.bookService;
    }

    public UserService getUserService() {
        return this.userService;
    }
}
