package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.request.services.RequestService;
import com.andersentask.bookshop.user.repository.UserRepository;
import com.andersentask.bookshop.user.service.UserService;
import com.andersentask.bookshop.utils.serialization.RepositoryDeserializer;
import com.andersentask.bookshop.utils.serialization.RepositorySerializer;

import java.math.BigDecimal;

public class ConsoleAppContextConfig {

    private final BookService bookService;

    private final UserService userService;

    private final OrderService orderService;

    private final RequestService requestService;

    private final EntityFactory entityFactory;

    private final RepositorySerializer serializer;

    public ConsoleAppContextConfig() {
        RepositoryDeserializer deserializer = new RepositoryDeserializer();
        this.bookService = new BookService(deserializer.deserializeAndWriteToBookRepository("books.json"));
        this.userService = new UserService(new UserRepository());
        this.orderService = new OrderService(deserializer.deserializeAndWriteToOrderRepository("orders.json"));
        this.requestService = new RequestService(deserializer.deserializeAndWriteToRequestRepository("requests.json"));
        this.entityFactory = new EntityFactory();
        this.serializer = new RepositorySerializer();
        setupBookService();
    }

    private void setupBookService() {
        if (bookService.getAllBooks().isEmpty()) {
            this.bookService.save(Book.builder()
                    .price(BigDecimal.valueOf(123.0))
                    .name("Gone with the Wind")
                    .status(BookStatus.AVAILABLE)
                    .build());
            this.bookService.save(Book.builder()
                    .price(BigDecimal.valueOf(264.0))
                    .name("Jane Eyre")
                    .status(BookStatus.AVAILABLE)
                    .build());
            this.bookService.save(Book.builder()
                    .price(BigDecimal.valueOf(1128.0))
                    .name("Pride and Prejudice")
                    .status(BookStatus.NOT_AVAILABLE)
                    .build());
            this.bookService.save(Book.builder()
                    .price(BigDecimal.valueOf(923.0))
                    .name("To Kill a Mockingbird")
                    .status(BookStatus.NOT_AVAILABLE)
                    .build());
        }
    }

    public BookService getBookService() {
        return this.bookService;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public RequestService getRequestService() {
        return requestService;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public RepositorySerializer getSerializer() {return serializer; }
}