package com.andersentask.bookshop.order.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.common.CollectionRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class OrderRepository implements CollectionRepository<Order, Long> {

    private final DataSource dataSource;

    private final BookService bookService;


    @Override
    public Optional<Order> findById(Long orderId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_BY_ID)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = OrderMapper.orderMapper.apply(resultSet);
                    Long numberOfBooksInOrder = countBooksInOneOrder(orderId, connection);
                    List<Book> booksInOrder = findBooks(resultSet, numberOfBooksInOrder);
                    order.setBooksInOrder(booksInOrder);
                    return Optional.of(order);
                } return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = OrderMapper.orderMapper.apply(resultSet);
                Long orderId = order.getOrderId();
                Long numberOfBooksInOrder = countBooksInOneOrder(orderId, connection);
                List<Book> booksInOrder = findBooks(resultSet, numberOfBooksInOrder);
                order.setBooksInOrder(booksInOrder);
            }
            return orders;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private Long countBooksInOneOrder(Long orderId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_COUNT_BY_ID)) {
            statement.setLong(1, orderId);
            var resultSet = statement.executeQuery();
            return resultSet.getLong("count");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private List<Book> findBooks(ResultSet resultSet, Long numberOfBooksInOrder) {
        List<Book> books = new ArrayList<>();
        try {
            books.add(findBook(resultSet));
            for (int i = 0; i < numberOfBooksInOrder - 1; i++) {
                resultSet.next();
                books.add(findBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private Book findBook(ResultSet resultSet) {
        try {
            Long bookId = resultSet.getLong("book_id");
            return bookService.getBookById(bookId).orElseThrow();
        } catch (   SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public Order save(Order obj) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(OrderSQLCommands.SQL_INSERT
                     , Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, obj.getUser());
            statement.setBigDecimal(2, obj.getOrderCost());
            statement.setString(3, obj.getOrderStatus().toString());
            statement.setTimestamp(4, Timestamp.valueOf(obj.getTimeOfCompletingOrder()));


        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    // it can be replaced just with findById.getBooks();
    // useless method
    public List<Book> findAllBooksFromOrder(Long id) {
        return findById(id).map(Order::getBooksInOrder)
                .orElse(new ArrayList<>());
    }

    public List<Order> getSortedOrders(OrderSort orderSort) {
        switch (orderSort) {
            case COST -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getOrderCost))
                        .toList();
            }
            case COMPLETION_DATE -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getTimeOfCompletingOrder).reversed())
                        .toList();
            }
            case STATUS -> {
                return orders.stream()
                        .sorted(Comparator.comparing(order -> order.getOrderStatus().ordinal()))
                        .toList();
            }
            case ID -> {
                return orders.stream()
                        .sorted(Comparator.comparing(Order::getOrderId))
                        .toList();
            }
        }
        return orders;
    }

}
