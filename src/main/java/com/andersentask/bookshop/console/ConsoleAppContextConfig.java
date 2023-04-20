package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.request.repository.RequestRepository;
import com.andersentask.bookshop.request.services.RequestService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class ConsoleAppContextConfig {

    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    private static final String DB_USER = "postgres";

    private static final String DB_PASSWORD = "31150616";

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookstore";

    private final BookService bookService;

    private final OrderService orderService;

    private final RequestService requestService;

    private final EntityFactory entityFactory;

    private final HikariDataSource dataSource;

    public ConsoleAppContextConfig() {
        dataSource = new HikariDataSource(getHikariConfig());
        liquibase();
        this.bookService = new BookService(new BookRepository(dataSource));
        this.requestService = new RequestService(new RequestRepository(dataSource, bookService));
        this.orderService = new OrderService(new OrderRepository(dataSource, bookService));
        this.entityFactory = new EntityFactory();
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

    public void closeDataSource() {
        dataSource.close();
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

    private void liquibase() {
        try (Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()))) {
            Liquibase liquibase = new liquibase
                    .Liquibase
                    ("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Can't apply liquibase");
        } catch (LiquibaseException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}