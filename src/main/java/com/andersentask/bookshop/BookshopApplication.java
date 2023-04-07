package com.andersentask.bookshop;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookCollectionRepositoryImpl;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.implementations.BookServiceImpl;
import com.andersentask.bookshop.book.services.interfaces.BookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Properties;

public class BookshopApplication {

    public static void main(String[] args) {
        BookCollectionRepositoryImpl bookRepository = new BookCollectionRepositoryImpl();
        BookService service = new BookServiceImpl(bookRepository);

        Book book = new Book(1l, "A", BookStatus.AVAILABLE, 1125.0);
        Book book1 = new Book(2l, "B", BookStatus.NOT_AVAILABLE, 225.0);
        Book book2 = new Book(3l, "C", BookStatus.AVAILABLE, 25.0);

        service.save(book);
        service.save(book1);
        service.save(book2);

        System.out.println(service.getAllBooks());
        System.out.println(service.getBooksSortedByPrice());
        System.out.println(service.getBooksSortedByAvailability());
        System.out.println(service.getBooksSortedByName());

    }

}
