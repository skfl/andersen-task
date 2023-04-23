package com.andersentask.bookshop;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.broker.EntityFactory;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.request.services.RequestService;
import jakarta.persistence.EntityManager;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

@Slf4j
public class AppContextConfig {

    private final BookService bookService;

    private final OrderService orderService;

    private  RequestService requestService;

    private final EntityFactory entityFactory;

    private final SessionFactory sessionFactory;

    public AppContextConfig() {
        sessionFactory = getHibernateSessionFactory();
        EntityManager entityManager =  sessionFactory.createEntityManager();
        liquibase();
        this.bookService = new BookService(new BookRepository(entityManager));
//        this.requestService = new RequestService(new RequestRepository(dataSource, bookService));
        this.orderService = new OrderService(new OrderRepository(entityManager));
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

    private void liquibase() {
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().doWork(connection -> {
            connection.setAutoCommit(false);
            try (Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
                Liquibase liquibase = new Liquibase
                        ("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
                connection.commit();
            } catch (LiquibaseException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw new IllegalArgumentException(e);
            }
        });
    }

    private SessionFactory getHibernateSessionFactory() {

        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        properties.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        properties.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/bookstore");
        properties.put(AvailableSettings.USER, "postgres");
        properties.put(AvailableSettings.PASS, "123321");
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(AvailableSettings.SHOW_SQL, "true");
        properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//        properties.put(AvailableSettings.HBM2DDL_AUTO, "none");
        properties.put(AvailableSettings.CONNECTION_PROVIDER, "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        properties.put(AvailableSettings.POOL_SIZE, 20);

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Order.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}