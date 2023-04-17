package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.request.services.RequestService;
import com.andersentask.bookshop.utils.serialization.RepositoryDeserializer;
import com.andersentask.bookshop.utils.serialization.RepositorySerializer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.math.BigDecimal;

public class ConsoleAppContextConfig {

    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    private static final String DB_USER = "postgres";

    private static final String DB_PASSWORD = "qwerty007";

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pcs_test";

    private final BookService bookService;

    private final OrderService orderService;

    private final RequestService requestService;

    private final EntityFactory entityFactory;

    private final RepositorySerializer serializer;

    public ConsoleAppContextConfig() {
        RepositoryDeserializer deserializer = new RepositoryDeserializer();
        this.bookService = new BookService(deserializer.deserializeAndWriteToBookRepository("books.json"));
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
                    .status(BookStatus.OUT_OF_STOCK)
                    .build());
            this.bookService.save(Book.builder()
                    .price(BigDecimal.valueOf(923.0))
                    .name("To Kill a Mockingbird")
                    .status(BookStatus.OUT_OF_STOCK)
                    .build());
        }
    }

    public BookService getBookService() {
        return this.bookService;
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

    public RepositorySerializer getSerializer() {
        return serializer;
    }

    private HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setDriverClassName(ORG_POSTGRESQL_DRIVER);
        config.setJdbcUrl(DB_URL);
        config.setMaximumPoolSize(20);
        return config;
    }

    private HikariDataSource getHikariDataSource() {
        return new HikariDataSource(getHikariConfig());
    }
}